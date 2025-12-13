/*
*  @(#)TokenUseCase.java
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
package br.com.jtech.tasklist.application.core.usecases.token;

import org.springframework.http.HttpHeaders;

import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.token.LogoutInputGateway;
import br.com.jtech.tasklist.application.ports.output.token.LogoutOutputGateway;
import br.com.jtech.tasklist.config.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
* class TokenUseCase  
* 
* user flavio.augusto  
*/
@RequiredArgsConstructor
public class LogoutUseCase implements LogoutInputGateway {

    private final LogoutOutputGateway logoutOutputGateway;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String token = authHeader.substring(7);

        String userEmail = jwtService.extractUsername(token);
        if (userEmail == null) {
            return;
        }

        User user = logoutOutputGateway.findByEmail(userEmail)
                .orElse(null);

        if (user == null) {
            return;
        }

        logoutOutputGateway.revokeAllTokens(user);
    }
}