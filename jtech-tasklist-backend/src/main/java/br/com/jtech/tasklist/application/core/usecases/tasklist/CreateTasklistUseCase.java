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
import br.com.jtech.tasklist.application.ports.input.tasklist.CreateTasklistInputGateway;
import br.com.jtech.tasklist.application.ports.output.tasklist.CreateTasklistOutputGateway;

/**
* class TasklistUseCase  
* 
* user angelo.vicente  
*/
public class CreateTasklistUseCase implements CreateTasklistInputGateway {

    private final CreateTasklistOutputGateway createTasklistOutputGateway;

    public CreateTasklistUseCase(CreateTasklistOutputGateway createTasklistOutputGateway) {
        this.createTasklistOutputGateway = createTasklistOutputGateway;
     }

    public Tasklist create(Tasklist tasklist) {
        return createTasklistOutputGateway.create(tasklist);
     }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return createTasklistOutputGateway.findByEmail(email);
    }
 }