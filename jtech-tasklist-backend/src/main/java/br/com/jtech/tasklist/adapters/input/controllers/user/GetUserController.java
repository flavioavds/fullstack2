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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.adapters.input.protocols.UserResponse;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.user.GetUserInputGateway;
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
public class GetUserController {
	
	private final GetUserInputGateway getUserInputGateway;
	
	@GetMapping("/{id}")
	@Operation(
	        operationId = "11_get_user_by_id",
	        summary = "Obter usuário por ID",
	        description = """
	            Recupera os dados de um usuário específico pelo seu ID.
	            
	            Observações:
	            - Endpoint protegido (JWT obrigatório)
	            - Apenas usuários com permissão podem acessar os dados
	            """,
	        responses = {
	            @ApiResponse(
	                responseCode = "200",
	                description = "Usuário encontrado",
	                content = @Content(
	                    mediaType = "application/json",
	                    schema = @Schema(implementation = UserResponse.class),
	                    examples = @ExampleObject(
	                        name = "Exemplo de Usuário",
	                        value = """
	                            {
	                              "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
	                              "name": "Flavio Souza",
	                              "email": "flavio.souza@email.com"
	                            }
	                        """
	                    )
	                )
	            ),
	            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
	            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
	            @ApiResponse(responseCode = "403", description = "Usuário não autorizado"),
	            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
	        }
	    )
	public ResponseEntity<UserResponse> getById(@PathVariable("id") UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserEmail = authentication.getName();

        User loggedUser = getUserInputGateway.findByEmail(loggedUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        if (!loggedUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este usuário");
        }

        User user = getUserInputGateway.getById(id);

        return user != null ? ResponseEntity.ok(UserResponse.of(user)) : ResponseEntity.notFound().build();
    }
	
	@GetMapping("/me")
    @Operation(
        operationId = "11_get_user_logged",
        summary = "Obter dados do usuário logado",
        description = """
            Recupera os dados do usuário logado no sistema.
            
            Observações:
            - Endpoint protegido (JWT obrigatório)
            - Retorna os dados do próprio usuário
            """,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
                )
            ),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
        }
    )
    public ResponseEntity<UserResponse> getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserEmail = authentication.getName();

        User loggedUser = getUserInputGateway.findByEmail(loggedUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        return ResponseEntity.ok(UserResponse.of(loggedUser));
    }
	
}
