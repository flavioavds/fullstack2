/*
*  @(#)UserOutputGateway.java
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
package br.com.jtech.tasklist.application.ports.output.user;

/**
* class UserOutputGateway 
* 
* user flavio.augusto 
*/
public interface ExistsUserByEmailOutputGateway {
	/**
     * Verifica se já existe usuário com o e-mail informado.
     *
     * @param email e-mail a ser verificado
     * @return true se existir usuário com o e-mail; false caso contrário
     */
    boolean existsByEmail(String email);

}
