/*
*  @(#)TasklistUseCase.java
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
package br.com.jtech.tasklist.application.core.usecases.tasklist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.DeleteTasklistInputGateway;
import br.com.jtech.tasklist.application.ports.output.tasklist.DeleteTasklistOutputGateway;

/**
* class TasklistUseCase  
* 
* user flavio.augusto  
*/
public class DeleteTasklistUseCase implements DeleteTasklistInputGateway{

	private final DeleteTasklistOutputGateway deleteTasklistOutputGateway;
		
	public DeleteTasklistUseCase(DeleteTasklistOutputGateway deleteTasklistOutputGateway) {
		this.deleteTasklistOutputGateway = deleteTasklistOutputGateway;
	}

	@Override
	public void delete(String id) {
		deleteTasklistOutputGateway.delete(id);		
	}
	
	@Override
    public Optional<User> findByEmail(String email) {
        return deleteTasklistOutputGateway.findByEmail(email);
    }

	@Override
    public Optional<Tasklist> findById(String id) {
        return deleteTasklistOutputGateway.findById(id); 
    }
	
	@Override
	public List<Tasklist> listAll(UUID userId) {
	    return deleteTasklistOutputGateway.listAll(userId);
	}
}
