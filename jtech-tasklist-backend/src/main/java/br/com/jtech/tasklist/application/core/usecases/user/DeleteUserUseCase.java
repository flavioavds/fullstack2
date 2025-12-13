/*
*  @(#)UserUseCase.java
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
package br.com.jtech.tasklist.application.core.usecases.user;

import java.util.Optional;
import java.util.UUID;

import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.user.DeleteUserInputGateway;
import br.com.jtech.tasklist.application.ports.output.user.DeleteUserOutputGateway;

/**
* class UserUseCase  
* 
* user flavio.augusto  
*/
public class DeleteUserUseCase implements DeleteUserInputGateway {
	
    private final DeleteUserOutputGateway outputGateway;

    public DeleteUserUseCase(DeleteUserOutputGateway outputGateway) {
		this.outputGateway = outputGateway;
	}

	@Override
    public void delete(UUID id) {
        outputGateway.delete(id);
    }
	
	@Override
    public Optional<User> findByEmail(String email) {
        return outputGateway.findByEmail(email);
    }
}