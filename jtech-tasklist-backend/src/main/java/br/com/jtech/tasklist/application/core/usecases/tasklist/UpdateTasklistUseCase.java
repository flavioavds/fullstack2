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

import java.util.Optional;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.UpdateTasklistInputGateway;
import br.com.jtech.tasklist.application.ports.output.tasklist.UpdateTasklistOutputGateway;

/**
* class TasklistUseCase  
* 
* user flavio.augusto  
*/
public class UpdateTasklistUseCase implements UpdateTasklistInputGateway{

	private final UpdateTasklistOutputGateway outputGateway;
		
	public UpdateTasklistUseCase(UpdateTasklistOutputGateway outputGateway) {
		this.outputGateway = outputGateway;
	}

	@Override
	public Tasklist update(String id, Tasklist tasklist) {
		return outputGateway.update(id, tasklist);
	}
	
	@Override
    public Optional<User> findByEmail(String email) {
        return outputGateway.findByEmail(email);
    }
	
	@Override
    public Optional<Tasklist> findById(String id) {
        return outputGateway.findById(id); 
    }
	
}
