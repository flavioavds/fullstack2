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

import java.io.IOException;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jtech.tasklist.adapters.input.protocols.AuthenticationResponse;
import br.com.jtech.tasklist.application.ports.input.token.RefreshTokenInputGateway;
import br.com.jtech.tasklist.application.ports.output.token.RefreshTokenOutputGateway;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
* class TokenUseCase  
* 
* user flavio.augusto  
*/
@RequiredArgsConstructor
public class RefreshTokenUseCase implements RefreshTokenInputGateway {

    private final RefreshTokenOutputGateway refreshTokenOutputGateway;

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            return;
        }

        final String refreshToken = header.substring(7);

        String userEmail = refreshTokenOutputGateway.extractEmail(refreshToken);

        if (userEmail != null) {
            var user = refreshTokenOutputGateway.findByEmail(userEmail)
                    .orElseThrow();

            if (refreshTokenOutputGateway.isTokenValid(refreshToken, user)) {

                var newAccessToken = refreshTokenOutputGateway.generateToken(user);

                refreshTokenOutputGateway.revokeOldTokens(user);
                refreshTokenOutputGateway.saveToken(user, newAccessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}