/*
*  @(#)Token.java
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
package br.com.jtech.tasklist.application.core.domains;

import java.util.UUID;

import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenType;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* class Token 
* 
* user flavio.augusto 
*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private Integer id;
    private String token;
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    private UUID userId;

    public TokenEntity toEntity(UserEntity user) {
        return TokenEntity.builder()
                .id(this.id)
                .token(this.token)
                .tokenType(this.tokenType != null ? this.tokenType : TokenType.BEARER)
                .expired(this.expired)
                .revoked(this.revoked)
                .user(user)
                .build();
    }

    public static Token of(TokenEntity entity) {
        return Token.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .tokenType(entity.getTokenType())
                .expired(entity.isExpired())
                .revoked(entity.isRevoked())
                .userId(entity.getUser().getId())
                .build();
    }
}