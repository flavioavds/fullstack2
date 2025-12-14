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

import java.util.UUID;

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

import br.com.jtech.tasklist.adapters.input.protocols.UserRequest;
import br.com.jtech.tasklist.adapters.input.protocols.UserResponse;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.user.UpdateUserInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class UpdateUserController {
	
	private final UpdateUserInputGateway updateUserInputGateway;
	
	@PutMapping("/{id}")
	@Operation(
	        operationId = "13_update_user",
	        summary = "Atualizar usuário",
	        description = """
	            Atualiza os dados de um usuário existente no sistema.
	            Em caso de Atualização de email ou senha será necessario logar novamente (Evite problemas com token)
	            
	            Observações:
	            - Endpoint protegido (JWT obrigatório)
	            - Apenas usuários com seu proprio token validado com seu id tem como permissão para atualizar
	            - Retorna 404 se o usuário não existir
	            """,
	        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	            required = true,
	            description = "Dados atualizados do usuário",
	            content = @Content(
	                mediaType = "application/json",
	                schema = @Schema(implementation = UserRequest.class),
	                examples = @ExampleObject(
	                    name = "Atualização de Usuário",
	                    value = """
	                        {
	                          "name": "Flavio Augusto Venancio de Souza",
	                          "email": "flavio.souza@gmail.com",
	                          "password": "Fla123"
	                        }
	                    """
	                )
	            )
	        ),
	        responses = {
	            @ApiResponse(
	                responseCode = "200",
	                description = "Usuário atualizado com sucesso",
	                content = @Content(
	                    mediaType = "application/json",
	                    schema = @Schema(implementation = UserResponse.class),
	                    examples = @ExampleObject(
	                        name = "Usuário Atualizado",
	                        value = """
	                            {
	                              "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
	                              "name": "Flavio Augusto Venancio de Souza",
	                              "email": "flavio.souza@gmail.com"
	                            }
	                        """
	                    )
	                )
	            ),
	            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
	            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
	            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
	            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
	            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	        }
	    )
	public ResponseEntity<UserResponse> update(
	        @PathVariable("id") UUID id, 
	        @RequestBody UserRequest request) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedUserEmail = authentication.getName(); 

	    User loggedUser = updateUserInputGateway.findByEmail(loggedUserEmail)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

	    if (!loggedUser.getId().equals(id)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para atualizar outro usuário");
	    }

	    User updated = updateUserInputGateway.update(id, request.toDomain());
	    return updated != null ? ResponseEntity.ok(UserResponse.of(updated)) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/me")
	@Operation(
	    operationId = "13_update_user_logged",
	    summary = "Atualizar dados do usuário logado",
	    description = """
	        Atualiza os dados do usuário logado no sistema.
	        Em caso de Atualização de email ou senha será necessario logar novamente (Evite problemas com token)
	        
	        Observações:
	        - Endpoint protegido (JWT obrigatório)
	        - Usuário só pode atualizar seus próprios dados
	        - Retorna 404 se o usuário não existir
	        """,
	        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		            required = true,
		            description = "Dados atualizados do usuário",
		            content = @Content(
		                mediaType = "application/json",
		                schema = @Schema(implementation = UserRequest.class),
		                examples = @ExampleObject(
		                    name = "Atualização de Usuário",
		                    value = """
		                        {
		                          "name": "Flavio Augusto Venancio de Souza",
		                          "email": "flavio.souza@gmail.com",
		                          "password": "Fla123"
		                        }
		                    """
		                )
		            )
		        ),
		        responses = {
		            @ApiResponse(
		                responseCode = "200",
		                description = "Usuário atualizado com sucesso",
		                content = @Content(
		                    mediaType = "application/json",
		                    schema = @Schema(implementation = UserResponse.class),
		                    examples = @ExampleObject(
		                        name = "Usuário Atualizado",
		                        value = """
		                            {
		                              "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
		                              "name": "Flavio Augusto Venancio de Souza",
		                              "email": "flavio.souza@gmail.com"
		                            }
		                        """
		                    )
		                )
		            ),
		            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
		            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
		            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
		            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
		            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
		        }
		    )
	public ResponseEntity<UserResponse> updateLoggedUser(@RequestBody UserRequest request) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedUserEmail = authentication.getName();

	    User loggedUser = updateUserInputGateway.findByEmail(loggedUserEmail)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

	    User updated = updateUserInputGateway.update(loggedUser.getId(), request.toDomain());

	    return ResponseEntity.ok(UserResponse.of(updated));
	}


}
