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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jtech.tasklist.adapters.input.protocols.UserRequest;
import br.com.jtech.tasklist.adapters.input.protocols.UserResponse;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.auth.RegisterUserInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
* class AuthController
* 
* user flavio.augusto
*/
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Autenticação e gerenciamento de usuários")
public class RegisterAuthUserController {

    private final RegisterUserInputGateway registerUserInputGateway;

    @PostMapping("/register")
    @Operation(
            operationId = "00_register_user",
            summary = "Cadastro de novo usuário",
            description = """
                Realiza o cadastro de um novo usuário no sistema.

                Este endpoint cria um usuário com base nos dados informados,
                aplicando regras internas de segurança e padronização.

                Regras aplicadas automaticamente:
                - Perfil (role): USER
                - Tipo de usuário: INTERNAL

                Observações:
                - Endpoint público
                - Não requer autenticação
                - Roles e tipo de usuário não podem ser definidos pelo cliente
                """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "Dados necessários para cadastro do usuário",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRequest.class),
                    examples = @ExampleObject(
                        name = "Cadastro de Usuário",
                        value = """
                            {
                              "name": "Flavio Souza",
                              "email": "flavio.souza@email.com",
                              "password": "123456"
                            }
                            """
                    )
                )
            ),
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserResponse.class),
                        examples = @ExampleObject(
                            name = "Usuário Criado",
                            value = """
                                {
                                  "id": 3fa85f64-5717-4562-b3fc-2c963f66afa6,
                                  "name": "Flavio Souza",
                                  "email": "flavio.souza@email.com"
                                }
                                """
                        )
                    )
                ),
                @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou obrigatórios não informados"
                ),
                @ApiResponse(
                    responseCode = "409",
                    description = "Usuário já existente (email duplicado)"
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno no servidor"
                )
            }
    )
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {

        User domainUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .userType(request.getUserType())
                .build();

        User saved = registerUserInputGateway.register(domainUser);

        UserResponse response = UserResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}