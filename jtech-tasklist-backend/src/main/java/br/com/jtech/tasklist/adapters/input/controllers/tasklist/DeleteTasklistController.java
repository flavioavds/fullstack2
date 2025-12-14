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

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.application.ports.input.tasklist.DeleteTasklistInputGateway;
import io.swagger.v3.oas.annotations.Operation;
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
public class DeleteTasklistController {
	
	private final DeleteTasklistInputGateway deleteTasklistInputGateway;
	
	@DeleteMapping("/{id}")
	@Operation(
	        operationId = "02_delete_tasklist",
	        summary = "Remover lista de tarefas",
	        description = """
	            Remove uma lista de tarefas existente.

	            A exclusão só é permitida se:
	            - O usuário estiver autenticado
	            - A lista pertencer ao usuário logado

	            Observações:
	            - Endpoint protegido (JWT obrigatório)
	            - Exclusão lógica ou física depende da regra de negócio
	            """,
	        parameters = {
	            @io.swagger.v3.oas.annotations.Parameter(
	                name = "id",
	                description = "Identificador único da lista de tarefas",
	                required = true,
	                example = "id"
	            )
	        },
	        responses = {
	            @ApiResponse(
	                responseCode = "204",
	                description = "Lista de tarefas removida com sucesso"
	            ),
	            @ApiResponse(
	                responseCode = "400",
	                description = "Identificador inválido"
	            ),
	            @ApiResponse(
	                responseCode = "401",
	                description = "Usuário não autenticado"
	            ),
	            @ApiResponse(
	                responseCode = "403",
	                description = "Usuário não autorizado a remover esta lista"
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
	public ResponseEntity<?> delete(@PathVariable("id") String id) {

	    var authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedUserEmail = authentication.getName();

	    var loggedUser = deleteTasklistInputGateway.findByEmail(loggedUserEmail)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado"));

	    deleteTasklistInputGateway.listAll(loggedUser.getId())
	            .stream()
	            .filter(t -> t.getId().toString().equals(id))
	            .findFirst()
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Tasklist não pertence ao usuário"));

	    deleteTasklistInputGateway.delete(id);

	    return ResponseEntity.ok().body(
	        Map.of(
	            "message", "Tasklist excluída com sucesso",
	            "tasklistId", id
	        )
	    );
	}


}
