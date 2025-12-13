package br.com.jtech.tasklist.config.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jtech.tasklist.adapters.input.protocols.AuthenticationRequest;
import br.com.jtech.tasklist.adapters.input.protocols.AuthenticationResponse;
import br.com.jtech.tasklist.adapters.input.protocols.RegisterRequest;
import br.com.jtech.tasklist.adapters.input.protocols.UserRequest;
import br.com.jtech.tasklist.adapters.output.repositories.TokenRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenType;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.roles.Role;
import br.com.jtech.tasklist.config.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  
  public AuthenticationResponse register(UserRequest dto) {
      if (repository.findByEmailIgnoreCase(dto.getEmail()).isPresent()) {
          throw new DataIntegrityViolationException("Email já registrado");
      }

      UserEntity user = UserEntity.builder()
              .name(dto.getName())
              .email(dto.getEmail())
              .password(passwordEncoder.encode(dto.getPassword()))
              .roles(getUserRoles(dto.getRoles()))
              .enabled(true)
              .build();

      UserEntity savedUser = repository.save(user);
      String jwtToken = jwtService.generateToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);
      saveUserToken(savedUser, jwtToken);

      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .build();
  }
  
  private RegisterRequest convertToUserDTORequest(RegisterRequest registerRequest) {
      return RegisterRequest.builder()
              .name(registerRequest.getName())
              .email(registerRequest.getEmail())
              .password(registerRequest.getPassword())
              .roles(registerRequest.getRoles())
              .userType(registerRequest.getUserType())
              .build();
  }

  public AuthenticationResponse register(RegisterRequest registerRequest) {
	  RegisterRequest userDTORequest = convertToUserDTORequest(registerRequest);
      return register(userDTORequest);
  }

  private Set<Role> getUserRoles(List<String> roles) {
      if (roles != null && !roles.isEmpty()) {
          return roles.stream()
                  .map(Role::valueOf)
                  .collect(Collectors.toSet());
      } else {
          return Collections.singleton(Role.USER);
      }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
	var user = repository.findByEmail(request.getEmail())
			   .orElseThrow(() -> new EntityNotFoundException("Usuário com email " + request.getEmail() + " não encontrado"));

	try {
	    authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(
	            request.getEmail(),
	            request.getPassword()
	        )
	    );
	} catch (BadCredentialsException ex) {
	    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha ou Email incorreta");
	}
	
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(UserEntity user, String jwtToken) {
    var token = TokenEntity.builder()
    	.user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(UserEntity user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
		    HttpServletRequest request,
		    HttpServletResponse response
		) throws IOException {
		    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
		        return;
		    }

		    final String refreshToken = authHeader.substring(7);
		    final String userEmail = jwtService.extractUsername(refreshToken);

		    if (userEmail != null) {
		        var user = this.repository.findByEmail(userEmail)
		                .orElseThrow();
		        if (jwtService.isTokenValid(refreshToken, user) && jwtService.isUserEnabled(user)) {
		            var accessToken = jwtService.generateToken(user);
		            revokeAllUserTokens(user);
		            saveUserToken(user, accessToken);
		            var authResponse = AuthenticationResponse.builder()
		                    .accessToken(accessToken)
		                    .refreshToken(refreshToken)
		                    .build();
		            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
		        }
		    }
		}
  
}
