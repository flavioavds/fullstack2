package br.com.jtech.tasklist.adapters.input.protocols;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.application.core.domains.Tasklist;

@DisplayName("TasklistResponse - Testes Unitários")
class TasklistResponseTest {

    @Nested
    @DisplayName("Método of(Tasklist)")
    class OfTasklistTests {

        @Test
        @DisplayName("Deve converter Tasklist para TasklistResponse")
        void shouldConvertTasklistToResponse() {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            
            Tasklist tasklist = Tasklist.builder()
                    .id(id)
                    .name("Minhas Tarefas")
                    .description("Lista de tarefas")
                    .completed(true)
                    .userId(userId)
                    .build();

            // Act
            TasklistResponse response = TasklistResponse.of(tasklist);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(id);
            assertThat(response.getName()).isEqualTo("Minhas Tarefas");
            assertThat(response.getDescription()).isEqualTo("Lista de tarefas");
            assertThat(response.isCompleted()).isTrue();
            assertThat(response.getUserId()).isEqualTo(userId);
        }
    }

    @Nested
    @DisplayName("Método of(List<TasklistEntity>)")
    class OfTasklistEntityListTests {

        @Test
        @DisplayName("Deve converter lista de TasklistEntity para TasklistResponse com lista")
        void shouldConvertEntityListToResponseWithList() {
            // Arrange
            TasklistEntity entity1 = new TasklistEntity();
            entity1.setId(UUID.randomUUID());
            entity1.setName("Entidade 1");
            entity1.setDesciption("Descrição 1");

            TasklistEntity entity2 = new TasklistEntity();
            entity2.setId(UUID.randomUUID());
            entity2.setName("Entidade 2");
            entity2.setDesciption("Descrição 2");

            List<TasklistEntity> entities = List.of(entity1, entity2);

            // Act
            TasklistResponse response = TasklistResponse.of(entities);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getResponses()).hasSize(2);
            assertThat(response.getResponses().get(0).getName()).isEqualTo("Entidade 1");
            assertThat(response.getResponses().get(1).getName()).isEqualTo("Entidade 2");
        }

        @Test
        @DisplayName("Deve lidar com lista vazia")
        void shouldHandleEmptyList() {
            // Arrange
            List<TasklistEntity> entities = List.of();

            // Act
            TasklistResponse response = TasklistResponse.of(entities);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getResponses()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Testes de Builder e Lombok")
    class BuilderTests {

        @Test
        @DisplayName("Deve criar objeto usando builder")
        void shouldCreateObjectUsingBuilder() {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID userId = UUID.randomUUID();

            // Act
            TasklistResponse response = TasklistResponse.builder()
                    .id(id)
                    .name("Response")
                    .description("Descrição do Response")
                    .completed(false)
                    .userId(userId)
                    .build();

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(id);
            assertThat(response.getName()).isEqualTo("Response");
            assertThat(response.getDescription()).isEqualTo("Descrição do Response");
            assertThat(response.isCompleted()).isFalse();
            assertThat(response.getUserId()).isEqualTo(userId);
        }

        @Test
        @DisplayName("Deve criar objeto com lista de responses")
        void shouldCreateObjectWithResponsesList() {
            // Arrange
            TasklistResponse nestedResponse1 = TasklistResponse.builder()
                    .name("Nested 1")
                    .build();

            TasklistResponse nestedResponse2 = TasklistResponse.builder()
                    .name("Nested 2")
                    .build();

            List<TasklistResponse> responses = List.of(nestedResponse1, nestedResponse2);

            // Act
            TasklistResponse response = TasklistResponse.builder()
                    .name("Parent")
                    .responses(responses)
                    .build();

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getResponses()).hasSize(2);
            assertThat(response.getResponses().get(0).getName()).isEqualTo("Nested 1");
            assertThat(response.getResponses().get(1).getName()).isEqualTo("Nested 2");
        }
    }
}