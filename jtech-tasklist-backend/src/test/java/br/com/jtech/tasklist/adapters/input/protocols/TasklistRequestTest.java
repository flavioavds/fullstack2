package br.com.jtech.tasklist.adapters.input.protocols;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.jtech.tasklist.application.core.domains.Tasklist;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TasklistRequest - Testes Unitários")
class TasklistRequestTest {

    @Nested
    @DisplayName("Testes de Conversão para Domain")
    class ConversionTests {

        @Test
        @DisplayName("Deve converter para Tasklist domain corretamente")
        void shouldConvertToDomainCorrectly() {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            
            TasklistRequest request = TasklistRequest.builder()
                    .id(id)
                    .name("Minhas Tarefas")
                    .description("Lista de tarefas diárias")
                    .completed(true)
                    .userId(userId)
                    .build();

            // Act
            Tasklist domain = request.toDomain();

            // Assert
            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isEqualTo(id);
            assertThat(domain.getName()).isEqualTo("Minhas Tarefas");
            assertThat(domain.getDescription()).isEqualTo("Lista de tarefas diárias");
            assertThat(domain.isCompleted()).isTrue();
            assertThat(domain.getUserId()).isEqualTo(userId);
        }

        @Test
        @DisplayName("Deve converter para Tasklist domain com campos nulos")
        void shouldConvertToDomainWithNullFields() {
            // Arrange
            TasklistRequest request = TasklistRequest.builder()
                    .name("Tarefas")
                    .description("Descrição")
                    .build();

            // Act
            Tasklist domain = request.toDomain();

            // Assert
            assertThat(domain).isNotNull();
            assertThat(domain.getId()).isNull();
            assertThat(domain.getName()).isEqualTo("Tarefas");
            assertThat(domain.getDescription()).isEqualTo("Descrição");
            assertThat(domain.isCompleted()).isFalse(); // Valor padrão
            assertThat(domain.getUserId()).isNull();
        }
    }

    @Nested
    @DisplayName("Testes de Validação")
    class ValidationTests {

        @Test
        @DisplayName("Deve ter nome não vazio")
        void shouldHaveNotEmptyName() {
            TasklistRequest request = TasklistRequest.builder()
                    .name("Nome Válido")
                    .description("Descrição")
                    .build();

            assertThat(request.getName()).isNotBlank();
        }

        @Test
        @DisplayName("Deve ter descrição não vazia")
        void shouldHaveNotEmptyDescription() {
            TasklistRequest request = TasklistRequest.builder()
                    .name("Nome")
                    .description("Descrição Válida")
                    .build();

            assertThat(request.getDescription()).isNotBlank();
        }
    }

    @Nested
    @DisplayName("Testes de Builder e Lombok")
    class BuilderTests {

        @Test
        @DisplayName("Deve criar objeto usando builder padrão")
        void shouldCreateObjectUsingBuilder() {
            // Act
            TasklistRequest request = TasklistRequest.builder()
                    .name("Teste")
                    .description("Descrição")
                    .build();

            // Assert
            assertThat(request).isNotNull();
            assertThat(request.getName()).isEqualTo("Teste");
            assertThat(request.getDescription()).isEqualTo("Descrição");
        }

        @Test
        @DisplayName("Deve criar objeto com todos os campos")
        void shouldCreateObjectWithAllFields() {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID userId = UUID.randomUUID();

            // Act
            TasklistRequest request = TasklistRequest.builder()
                    .id(id)
                    .name("Nome")
                    .description("Descrição")
                    .completed(true)
                    .userId(userId)
                    .build();

            // Assert
            assertThat(request.getId()).isEqualTo(id);
            assertThat(request.getName()).isEqualTo("Nome");
            assertThat(request.getDescription()).isEqualTo("Descrição");
            assertThat(request.isCompleted()).isTrue();
            assertThat(request.getUserId()).isEqualTo(userId);
        }
    }
}