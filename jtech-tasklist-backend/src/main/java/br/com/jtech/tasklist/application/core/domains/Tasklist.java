/*
*  @(#)Tasklist.java
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
* class Tasklist 
* 
* user angelo.vicente 
*/
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tasklist {

    private UUID id;
    private String name;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID userId;

    public static List<Tasklist> of(List<TasklistEntity> entities) {
        return entities.stream().map(Tasklist::of).toList();
     }
    
    public TasklistEntity toEntity() {
        TasklistEntity entity = TasklistEntity.builder()
            .id(this.id != null ? this.id : null)
            .name(this.name)
            .desciption(this.description)
            .completed(this.completed)
            .createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
            .updatedAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now())
            .build();

        if (this.userId != null) {
            UserEntity user = new UserEntity();
            user.setId(this.userId);
            entity.setUser(user);
        }

        return entity;
    }

    public static Tasklist of(TasklistEntity entity) {
        return Tasklist.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDesciption())
            .completed(entity.isCompleted())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .userId(entity.getUser() != null ? entity.getUser().getId() : null)
            .build();
     }

    public static Tasklist of(TasklistRequest request, UUID loggedUserId) {
        return Tasklist.builder()
            .id(request.getId())
            .name(request.getName())
            .description(request.getDescription())
            .completed(false) 
            .userId(loggedUserId) 
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

 }