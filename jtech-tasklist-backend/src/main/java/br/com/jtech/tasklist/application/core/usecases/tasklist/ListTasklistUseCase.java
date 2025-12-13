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
import br.com.jtech.tasklist.application.ports.input.tasklist.ListTasklistInputGateway;
import br.com.jtech.tasklist.application.ports.output.tasklist.ListTasklistOutputGateway;

/**
* class TasklistUseCase  
* 
* user flavio.augusto  
*/
public class ListTasklistUseCase implements ListTasklistInputGateway{
	
	private final ListTasklistOutputGateway listTasklistOutputGateway;
	
	public ListTasklistUseCase(ListTasklistOutputGateway listTasklistOutputGateway) {
		super();
		this.listTasklistOutputGateway = listTasklistOutputGateway;
	}

	@Override
	public List<Tasklist> listAll() {
	    return listTasklistOutputGateway.listAll();
	}

	@Override
	public List<Tasklist> listAll(UUID userId) {
	    return listTasklistOutputGateway.listAll(userId);
	}

	@Override
	public Optional<User> findByEmail(String email) {
	    return listTasklistOutputGateway.findByEmail(email);
	}

}
