/*
*  @(#)TasklistController.java
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
package br.com.jtech.tasklist.adapters.input.controllers.tasklist;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.CreateTasklistInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
* class TasklistController
* 
* user angelo.vicente
*/
@RestController
@RequestMapping("/api/v1/tasklists")
@RequiredArgsConstructor
@Tag(
	    name = "Tasklist",
	    description = "Gerenciamento de listas de tarefas"
	)
public class CreateTasklistController {

    private final CreateTasklistInputGateway createTasklistInputGateway;
    
    @PostMapping
    @Operation(
            operationId = "01_create_tasklist",
            summary = "Criação de lista de tarefas",
            description = """
                Cria uma nova lista de tarefas no sistema.

                A lista será associada automaticamente ao usuário autenticado.

                Regras aplicadas:
                - Endpoint protegido (requer autenticação JWT)
                - A lista pertence exclusivamente ao usuário logado
                - Não é permitido criar listas para outros usuários
                """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "Dados necessários para criação da lista de tarefas",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TasklistRequest.class),
                    examples = @ExampleObject(
                        name = "Criar Tasklist",
                        value = """
                            {
                              "name": "Minhas tarefas diárias",
                              "description": "Lista de tarefas pessoais"
                            }
                            """
                    )
                )
            ),
            responses = {
                @ApiResponse(
                    responseCode = "200",
                    description = "Lista de tarefas criada com sucesso",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TasklistResponse.class),
                        examples = @ExampleObject(
                            name = "Tasklist Criada",
                            value = """
                                {
                                  "id": "b0d1faaf-6e6e-4ab9-927a-1cdd0a864437",
                                  "name": "Minha lista que quero comprar",
                            	  "description": "Preciso de uma compra de café",
                            	  "completed": false,
                            	  "userId": "3543593c-9fff-426e-994e-fe5609e45718"
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
                    responseCode = "401",
                    description = "Usuário não autenticado"
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno no servidor"
                )
            }
        )
    public ResponseEntity<TasklistResponse> create(@Valid @RequestBody TasklistRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserEmail = authentication.getName();

        User loggedUser = createTasklistInputGateway.findByEmail(loggedUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        Tasklist domain = request.toDomain();

        domain.setUserId(loggedUser.getId());

        Tasklist created = createTasklistInputGateway.create(domain);

        return ResponseEntity.ok(TasklistResponse.of(created));
    }
    
 }