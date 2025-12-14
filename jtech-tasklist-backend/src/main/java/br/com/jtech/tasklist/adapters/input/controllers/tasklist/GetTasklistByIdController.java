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

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse;
import br.com.jtech.tasklist.application.ports.input.tasklist.GetTasklistByIdInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
* class TasklistController
* 
* user flavio.augusto
*/
@RestController
@RequestMapping("/api/v1/tasklists")
@RequiredArgsConstructor
@Tag(
	    name = "Tasklist",
	    description = "Gerenciamento de listas de tarefas"
	)
public class GetTasklistByIdController {
	
	private final GetTasklistByIdInputGateway getTasklistByIdInputGateway;
	
	@GetMapping(value =  "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	 @Operation(
		        operationId = "03_get_tasklist_by_id",
		        summary = "Consultar lista de tarefas por ID",
		        description = """
		            Recupera os detalhes de uma lista de tarefas específica.

		            A lista retornada deve:
		            - Existir no sistema
		            - Pertencer ao usuário autenticado

		            Observações:
		            - Endpoint protegido (JWT obrigatório)
		            - Retorna apenas dados do próprio usuário
		            """,
		        parameters = {
		            @io.swagger.v3.oas.annotations.Parameter(
		                name = "id",
		                description = "Identificador único da lista de tarefas (UUID)",
		                required = true,
		                example = "id"
		            )
		        },
		        responses = {
		            @ApiResponse(
		                responseCode = "200",
		                description = "Lista de tarefas encontrada com sucesso",
		                content = @Content(
		                    mediaType = "application/json",
		                    schema = @Schema(implementation = TasklistResponse.class),
		                    examples = @ExampleObject(
		                        name = "Tasklist encontrada",
		                        value = """
		                            {
		                              "id": "550e8400-e29b-41d4-a716-446655440000",
		                              "title": "Minhas tarefas",
		                              "description": "Lista de tarefas pessoais",
		                              "createdAt": "2025-01-10T14:30:00"
		                            }
		                            """
		                    )
		                )
		            ),
		            @ApiResponse(
		                responseCode = "400",
		                description = "Formato do identificador inválido"
		            ),
		            @ApiResponse(
		                responseCode = "401",
		                description = "Usuário não autenticado"
		            ),
		            @ApiResponse(
		                responseCode = "403",
		                description = "Usuário não autorizado a acessar esta lista"
		            ),
		            @ApiResponse(
		                responseCode = "404",
		                description = "Lista de tarefas não encontrada"
		            ),
		            @ApiResponse(
		                responseCode = "500",
		                description = "Erro interno no servidor"
		            )
		        }
		    )
	public ResponseEntity<Object> getById(@PathVariable("id") UUID id) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedUserEmail = authentication.getName();

	    var loggedUser = getTasklistByIdInputGateway.findByEmail(loggedUserEmail)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
	                    "{\"message\":\"Usuário não autenticado\",\"status\":401}"));

	    var tasklist = getTasklistByIdInputGateway.getById(id);
	    if (tasklist == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("{\"message\":\"Lista de tarefas não encontrada\",\"status\":404}");
	    }

	    if (!tasklist.getUserId().equals(loggedUser.getId())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body("{\"message\":\"Usuário não autorizado a acessar esta lista\",\"status\":403}");
	    }

	    return ResponseEntity.ok(TasklistResponse.of(tasklist));
	}


}
