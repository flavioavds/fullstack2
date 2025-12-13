/*
*  @(#)TokenOutputGateway.java
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
package br.com.jtech.tasklist.application.ports.output.token;

import java.util.Optional;

import br.com.jtech.tasklist.application.core.domains.User;

/**
* class TokenOutputGateway 
* 
* user flavio.augusto 
*/
public interface RefreshTokenOutputGateway {
    Optional<User> findByEmail(String email);
    boolean isTokenValid(String token, User user);
    String generateToken(User user);
    void revokeOldTokens(User user);
    void saveToken(User user, String token);
    String extractEmail(String token);

}