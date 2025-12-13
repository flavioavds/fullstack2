/*
*  @(#)AuthAdapter.java
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
package br.com.jtech.tasklist.adapters.output.auth;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.token.JwtTokenOutputGateway;
import br.com.jtech.tasklist.config.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;

/**
* class AuthAdapter 
* 
* user angelo.vicente  
*/
@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements JwtTokenOutputGateway {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public String generateToken(User user) {

        UserEntity entity = userRepository.findByEmailIgnoreCase(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtService.generateToken(entity);
    }

    @Override
    public String generateRefreshToken(User user) {

        UserEntity entity = userRepository.findByEmailIgnoreCase(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtService.generateRefreshToken(entity);
    }

    @Override
    public String extractUsername(String token) {
        return jwtService.extractUsername(token);
    }

    @Override
    public boolean isTokenValid(String token, User user) {

        UserEntity entity = userRepository.findByEmailIgnoreCase(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtService.isTokenValid(token, entity);
    }
}
