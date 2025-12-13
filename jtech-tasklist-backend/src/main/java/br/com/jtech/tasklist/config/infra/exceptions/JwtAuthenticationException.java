/*
 *  @(#)JwtAuthenticationException.java
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
package br.com.jtech.tasklist.config.infra.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Class used for JwtAuthenticationException to show errors API.
 *
 * @author flavio.augusto
 * class ApiError
 **/
public class JwtAuthenticationException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public JwtAuthenticationException(String message) {
        super(message);
    }
}