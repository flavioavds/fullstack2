package br.com.jtech.tasklist.config.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.config.infra.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.expiration}")
  private Long jwtExpiration;
  @Value("${jwt.refresh-token}")
  private Long refreshExpiration;
  
  @PostConstruct
  public void init() {
      logger.info("Secret key: {}", secretKey);
      logger.info("JWT expiration: {}", jwtExpiration);
      logger.info("Refresh token expiration: {}", refreshExpiration);
  }

  public String extractUsername(String token) throws JwtAuthenticationException{
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws JwtAuthenticationException {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) throws JwtAuthenticationException {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) throws JwtAuthenticationException {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(
      UserDetails userDetails
  ) throws JwtAuthenticationException {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
	) throws JwtAuthenticationException {
      try {
          return Jwts
                  .builder()
                  .setClaims(extraClaims)
                  .setSubject(userDetails.getUsername())
                  .setIssuedAt(new Date(System.currentTimeMillis()))
                  .setExpiration(new Date(System.currentTimeMillis() + expiration))
                  .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                  .claim("roles", userDetails.getAuthorities())
                  .claim("desenvolvedor", "flavio")
                  .compact();
      } catch (Exception e) {
          logger.error("Error generating token: {}", e.getMessage());
          throw new JwtAuthenticationException("Error generating token");
      }
  }

  public boolean isTokenValid(String token, UserDetails userDetails) throws JwtAuthenticationException {
	    final String username = extractUsername(token);
	    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && isUserEnabled(userDetails);
	}
  
  public boolean isUserEnabled(UserDetails userDetails) {
	    UserEntity user = (UserEntity) userDetails;
	    return user.isEnabled();
	}

  public boolean isTokenExpired(String token) {
	    try {
	        final Date expiration = extractExpiration(token);
	        return expiration.before(new Date());
	    } catch (ExpiredJwtException e) {
	        logger.error("Token expired: {}", e.getMessage());
	        return true;
	    } catch (Exception e) {
	        logger.error("Error checking token validity: {}", e.getMessage());
	        return true;
	    }
	}


  private Date extractExpiration(String token) throws JwtAuthenticationException {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
	    try {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSignInKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    } catch (ExpiredJwtException e) {
	        throw new JwtAuthenticationException("Token expirado");
	    } catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
	        throw new JwtAuthenticationException("Token inválido");
	    } catch (JwtException e) {
	        throw new JwtAuthenticationException("Erro ao processar o token");
	    }
	}

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}