/*
*  @(#)TasklistOutputGateway.java
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
package br.com.jtech.tasklist.application.ports.output.tasklist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;

/**
* class TasklistOutputGateway 
* 
* user flavio.augusto 
*/
public interface ListTasklistOutputGateway {
	List<Tasklist> listAll();
	List<Tasklist> listAll(UUID userId);
	Optional<User> findByEmail(String email);

}
