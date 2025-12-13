/*
*  @(#)AuthController.java
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
package br.com.jtech.tasklist.adapters.input.controllers.auth;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jtech.tasklist.application.ports.input.token.RefreshTokenInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
* class AuthController
* 
* user flavio.augusto
*/
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Autenticação e gerenciamento de sessão")
public class RefreshTokenController {

    private final RefreshTokenInputGateway refreshTokenInputGateway;

    @PostMapping("/refresh-token")
    @Operation(
            operationId = "02_refresh_token",
            summary = "Renova o token de acesso",
            description = "Gera um novo access token a partir de um refresh token válido"
        )
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        refreshTokenInputGateway.refresh(request, response);
    }
}
