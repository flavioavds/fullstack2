/*
*  @(#)UserUseCaseConfig.java
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
package br.com.jtech.tasklist.config.usecases.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.jtech.tasklist.adapters.output.user.ListUserAdapter;
import br.com.jtech.tasklist.application.core.usecases.user.ListUserUseCase;

/**
* class UserUseCaseConfig
* 
* user flavio.augusto
*/
@Configuration
public class ListUserUseCaseConfig {

	@Bean
	public ListUserUseCase listUserUseCase(ListUserAdapter listUserAdapter) {
		return new ListUserUseCase(listUserAdapter);
	}
}
