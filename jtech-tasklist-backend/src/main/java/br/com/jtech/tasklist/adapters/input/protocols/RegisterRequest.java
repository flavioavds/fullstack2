/*
*  @(#)RegisterRequest.java
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
package br.com.jtech.tasklist.adapters.input.protocols;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* class RegisterRequest 
* 
* user flavio.augusto 
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

		@NotBlank(message = "Nome não pode estar vazio")
	 	private String name;
		
		@NotBlank(message = "Email não pode estar vazio")
	    @Email(message = "Email inválido")
	    private String email;
		
		@NotBlank(message = "Senha não pode estar vazia")
	    private String password;
		
	    private List<String> roles;
	    private String userType;
}
