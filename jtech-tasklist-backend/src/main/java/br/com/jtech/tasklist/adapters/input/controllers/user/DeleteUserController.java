/*
*  @(#)UserController.java
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
package br.com.jtech.tasklist.adapters.input.controllers.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.user.DeleteUserInputGateway;
import br.com.jtech.tasklist.config.infra.exceptions.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
* class UserController
* 
* user angelo.vicente
*/
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(
	    name = "User",
	    description = "Gerenciamento de usuários do sistema"
	)
public class DeleteUserController {
	
	private final DeleteUserInputGateway deleteUserInputGateway;
	
	 @DeleteMapping("/{id}")
	 @Operation(
		        operationId = "10_delete_user",
		        summary = "Excluir usuário",
		        description = """
		            Exclui um usuário existente pelo ID.
		            
		            Observações:
		            - Endpoint protegido (JWT obrigatório)
		            - Usuário precisa ter permissões administrativas
		            """,
		        responses = {
		            @ApiResponse(
		                responseCode = "204",
		                description = "Usuário excluído com sucesso"
		            ),
		            @ApiResponse(
		                responseCode = "404",
		                description = "Usuário não encontrado"
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
	 public ResponseEntity<SuccessResponse> delete(
		        @Parameter(description = "ID do usuário a ser excluído", required = true)
		        @PathVariable("id") UUID id) {

		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String loggedUserEmail = authentication.getName();

		    User loggedUser = deleteUserInputGateway.findByEmail(loggedUserEmail)
		            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

		    if (loggedUser.getRoles() == null || !loggedUser.getRoles().contains("ADMIN")) {
		        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir usuários");
		    }

		    deleteUserInputGateway.delete(id);

		    SuccessResponse response = new SuccessResponse(
		            "SUCCESS",
		            "Usuário excluído com sucesso",
		            LocalDateTime.now()
		    );

		    return ResponseEntity.ok(response);
		}
}
