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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jtech.tasklist.application.ports.input.token.LogoutInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
* class AuthController
* 
* user flavio.augusto
*/
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Sair da conta logada.")
public class LogoutController {

    private final LogoutInputGateway logoutInputGateway;

    @PostMapping("/logout")
    @Operation(
            summary = "Realiza logout do usuário",
            description = "Invalida o token JWT informado no header Authorization. "
                        + "Após o logout, o token não poderá mais ser utilizado.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Token inválido ou ausente"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    public void logout(HttpServletRequest request) {
        logoutInputGateway.logout(request);
    }
}