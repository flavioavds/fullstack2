/*
*  @(#)TasklistEntity.java
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
package br.com.jtech.tasklist.adapters.output.repositories.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* class TasklistEntity 
* 
* @author angelo.vicente
*/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "TasklistEntity")
public class TasklistEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @NotBlank(message = "Nome do Titulo não pode estar vazio")
    @jakarta.validation.constraints.NotBlank
    @jakarta.persistence.Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Descrição da Tarefa não pode estar vazio")
    @jakarta.persistence.Column(length = 500)
    private String desciption;
    
    @Builder.Default
    @jakarta.persistence.Column(nullable = false)
    private boolean completed = false;
    
    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Builder.Default
    @jakarta.persistence.Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Builder.Default
    @jakarta.persistence.Column(nullable = false)
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

}