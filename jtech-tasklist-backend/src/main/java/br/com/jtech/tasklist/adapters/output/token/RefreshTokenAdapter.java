/*
*  @(#)TokenAdapter.java
*
*  Copyright (c) J-Tech Solucoes em Informatica.
*  All Rights Reserved.
*
*  This software is the confidential and proprietary information of J-Tech.
*  ("Confidential Information"). You shall not disclose such Confidential
*  Information and shall use it only in accordance with the terms of the
*  license agreement you entered into with J-Tech.
*
*/
package br.com.jtech.tasklist.adapters.output.token;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.TokenRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenType;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.token.RefreshTokenOutputGateway;
import br.com.jtech.tasklist.config.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;

/**
* class TokenAdapter 
* 
* user angelo.vicente  
*/
@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenOutputGateway {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(User::of);
    }

    @Override
    public boolean isTokenValid(String token, User user) {
        return jwtService.isTokenValid(token, user.toEntity(user.getPassword()));
    }

    @Override
    public String generateToken(User user) {
        return jwtService.generateToken(user.toEntity(user.getPassword()));
    }

    @Override
    public void revokeOldTokens(User user) {
        var tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        tokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(tokens);
    }

    @Override
    public void saveToken(User user, String token) {
        TokenEntity entity = TokenEntity.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user.toEntity(user.getPassword()))
                .build();

        tokenRepository.save(entity);
    }

    @Override
    public String extractEmail(String token) {
        return jwtService.extractUsername(token);
    }
}
