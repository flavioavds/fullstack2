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
import br.com.jtech.tasklist.application.ports.input.tasklist.CompletedTasklistInputGateway;
import br.com.jtech.tasklist.application.ports.output.tasklist.CompletedTasklistOutputGateway;

/**
* class TasklistUseCase  
* 
* user flavio.augusto  
*/
public class CompletedTasklistUseCase implements CompletedTasklistInputGateway {

    private final CompletedTasklistOutputGateway outputGateway;

    public CompletedTasklistUseCase(CompletedTasklistOutputGateway outputGateway) {
        this.outputGateway = outputGateway;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return outputGateway.findByEmail(email);
    }

    @Override
    public Tasklist completeTasklist(UUID tasklistId, UUID userId) {
        Tasklist tasklist = outputGateway.findByIdAndUserId(tasklistId, userId)
                .orElseThrow(() -> new RuntimeException("Tasklist não pertence ao usuário ou não existe"));

        tasklist.setCompleted(true);

        return outputGateway.update(tasklist);
    }
}