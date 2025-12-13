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

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse;
import br.com.jtech.tasklist.application.ports.input.tasklist.ListTasklistInputGateway;
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
public class ListTasklistController {
	
	private final ListTasklistInputGateway listTasklistInputGateway;
	
	 @GetMapping
	 @Operation(
		        operationId = "04_list_tasklists",
		        summary = "Listar listas de tarefas",
		        description = """
		            Retorna todas as listas de tarefas associadas ao usuário autenticado.

		            O resultado contém apenas listas que:
		            - Pertencem ao usuário logado
		            - Estão ativas no sistema

		            Observações:
		            - Endpoint protegido (JWT obrigatório)
		            - Retorna uma coleção de listas
		            """,
		        responses = {
		            @ApiResponse(
		                responseCode = "200",
		                description = "Listas de tarefas retornadas com sucesso",
		                content = @Content(
		                    mediaType = "application/json",
		                    schema = @Schema(implementation = TasklistResponse.class),
		                    examples = @ExampleObject(
		                        name = "Lista de Tasklists",
		                        value = """
		                            [
		                              {
		                                "id": "550e8400-e29b-41d4-a716-446655440000",
		                                "title": "Trabalho",
		                                "description": "Tarefas do trabalho"
		                              },
		                              {
		                                "id": "660e8400-e29b-41d4-a716-446655440001",
		                                "title": "Pessoal",
		                                "description": "Tarefas pessoais"
		                              }
		                            ]
		                            """
		                    )
		                )
		            ),
		            @ApiResponse(
		                responseCode = "401",
		                description = "Usuário não autenticado"
		            ),
		            @ApiResponse(
		                responseCode = "403",
		                description = "Usuário não autorizado"
		            ),
		            @ApiResponse(
		                responseCode = "500",
		                description = "Erro interno no servidor"
		            )
		        }
		    )
	 public ResponseEntity<List<TasklistResponse>> listAll() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String loggedUserEmail = authentication.getName();
	        
	        var loggedUser = listTasklistInputGateway.findByEmail(loggedUserEmail)
	                .orElseThrow(() -> new ResponseStatusException(
	                        HttpStatus.UNAUTHORIZED, 
	                        "Usuário não autenticado"));

	        var tasks = listTasklistInputGateway.listAll(loggedUser.getId());

	        var response = tasks.stream().map(TasklistResponse::of).toList();
	        return ResponseEntity.ok(response);
	    }

}
