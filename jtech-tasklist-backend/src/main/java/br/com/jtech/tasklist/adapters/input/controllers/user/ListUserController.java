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

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.adapters.input.protocols.UserResponse;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.user.ListUserInputGateway;
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
public class ListUserController {
	
	private final ListUserInputGateway listUserInputGateway;
	
	@GetMapping
	@Operation(
	        operationId = "12_list_users",
	        summary = "Listar todos os usuários",
	        description = """
	            Retorna uma lista de todos os usuários cadastrados no sistema.
	            
	            Observações:
	            - Endpoint protegido (JWT obrigatório)
	            - Apenas usuários ADMIN com permissão podem acessar os dados
	            """,
	        responses = {
	            @ApiResponse(
	                responseCode = "200",
	                description = "Lista de usuários retornada com sucesso",
	                content = @Content(
	                    mediaType = "application/json",
	                    schema = @Schema(implementation = UserResponse.class),
	                    examples = @ExampleObject(
	                        name = "Exemplo de Lista de Usuários",
	                        value = """
	                            [
	                              {
	                                "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
	                                "name": "Flávio Souza",
	                                "email": "flavio.souza@email.com"
	                              },
	                              {
	                                "id": "4bfae5a1-1234-4567-8910-abcdef123456",
	                                "name": "Ana Silva",
	                                "email": "ana.silva@email.com"
	                              }
	                            ]
	                        """
	                    )
	                )
	            ),
	            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
	            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
	            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	        }
	    )
	public ResponseEntity<List<UserResponse>> listAll() {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String loggedUserEmail = authentication.getName();

	    User loggedUser = listUserInputGateway.findByEmail(loggedUserEmail)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

	    if (loggedUser.getRoles() == null || !loggedUser.getRoles().contains("ADMIN")) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta lista");
	    }

	    List<User> users = listUserInputGateway.listAll();
	    List<UserResponse> response = users.stream().map(UserResponse::of).toList();
	    return ResponseEntity.ok(response);
	}

}
