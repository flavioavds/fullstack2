/*
 *  @(#)AuthenticationRequest.java
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

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* class AuthenticationRequest 
* 
* user flavio.augusto 
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

	@NotBlank(message = "Email não pode estar vazio")
	@Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
	@Email(message = "Email inválido")
	private String email;
	
	@NotBlank(message = "Senha não pode estar vazio")
	private String password;

}
