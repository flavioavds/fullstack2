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
import java.util.UUID;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.GetTasklistByIdInputGateway;
import br.com.jtech.tasklist.application.ports.output.tasklist.GetTasklistByIdOutputGateway;

/**
* class TasklistUseCase  
* 
* user flavio.augusto  
*/
public class GetTasklistByIdUseCase implements GetTasklistByIdInputGateway{
	
	private final GetTasklistByIdOutputGateway getTasklistByIdOutputGateway;
	
	public GetTasklistByIdUseCase(GetTasklistByIdOutputGateway getTasklistByIdOutputGateway) {
		super();
		this.getTasklistByIdOutputGateway = getTasklistByIdOutputGateway;
	}

	@Override
	public Tasklist getById(UUID id) {
		return getTasklistByIdOutputGateway.getById(id);
	}
	
	@Override
    public Optional<User> findByEmail(String email) {
        return getTasklistByIdOutputGateway.findByEmail(email);
    }

	@Override
    public Optional<Tasklist> findById(String id) {
        return getTasklistByIdOutputGateway.findById(id); 
    }

}
