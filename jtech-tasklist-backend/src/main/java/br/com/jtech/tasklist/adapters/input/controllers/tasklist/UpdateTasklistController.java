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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.UpdateTasklistInputGateway;
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
* user flavio.augusto
*/
@RestController
@RequestMapping("/api/v1/tasklists")
@RequiredArgsConstructor
@Tag(
	    name = "Tasklist",
	    description = "Gerenciamento de listas de tarefas"
	)
public class UpdateTasklistController {
	
	private final UpdateTasklistInputGateway updateTasklistInputGateway;
	
	@PutMapping("/{id}")
	@Operation(
	        operationId = "05_update_tasklist",
	        summary = "Atualizar lista de tarefas",
	        description = """
	            Atualiza os dados de uma lista de tarefas existente.
	            
	            Este endpoint permite alterar título, descrição e outros campos da lista de tarefas.
	            
	            Observações:
	            - Endpoint protegido (JWT obrigatório)
	            - Retorna 404 se a lista não existir
	            """,
	        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	            required = true,
	            description = "Dados da lista de tarefas a serem atualizados",
	            content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = TasklistRequest.class),
	                examples = @ExampleObject(
	                    name = "Atualização de Tasklist",
	                    value = """
	                        {
	                          "title": "Trabalho Atualizado",
	                          "description": "Tarefas do trabalho atualizadas"
	                        }
	                        """
	                )
	            )
	        ),
	        responses = {
	            @ApiResponse(
	                responseCode = "200",
	                description = "Lista de tarefas atualizada com sucesso",
	                content = @Content(
	                    mediaType = "application/json",
	                    schema = @Schema(implementation = TasklistResponse.class),
	                    examples = @ExampleObject(
	                        name = "Tasklist Atualizada",
	                        value = """
	                            {
	                              "id": "550e8400-e29b-41d4-a716-446655440000",
	                              "title": "Trabalho Atualizado",
	                              "description": "Tarefas do trabalho atualizadas"
	                            }
	                            """
	                    )
	                )
	            ),
	            @ApiResponse(
	                responseCode = "404",
	                description = "Lista de tarefas não encontrada"
	            ),
	            @ApiResponse(
	                responseCode = "400",
	                description = "Dados inválidos"
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
	public ResponseEntity<TasklistResponse> update(
	        @PathVariable("id") String id,
	        @RequestBody @Valid TasklistRequest request) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedUserEmail = authentication.getName();

	    User loggedUser = updateTasklistInputGateway.findByEmail(loggedUserEmail)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

	    Tasklist existingTasklist = updateTasklistInputGateway.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tasklist não encontrada"));

	    if (!existingTasklist.getUserId().equals(loggedUser.getId())) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para atualizar esta tasklist");
	    }

	    existingTasklist.setName(request.getName());
	    existingTasklist.setDescription(request.getDescription());

	    Tasklist updated = updateTasklistInputGateway.update(id, existingTasklist);

	    return ResponseEntity.ok(TasklistResponse.of(updated));
	}

}
