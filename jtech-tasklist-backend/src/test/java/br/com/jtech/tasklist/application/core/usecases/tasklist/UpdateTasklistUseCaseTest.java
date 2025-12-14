package br.com.jtech.tasklist.application.core.usecases.tasklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.tasklist.UpdateTasklistOutputGateway;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTasklistUseCase - Testes Unitários")
class UpdateTasklistUseCaseTest {

    @Mock
    private UpdateTasklistOutputGateway outputGateway;

    @InjectMocks
    private UpdateTasklistUseCase useCase;

    private User user;
    private Tasklist existingTasklist;
    private final UUID userId = UUID.randomUUID();
    private final UUID tasklistId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(userId)
                .email("usuario@email.com")
                .build();

        existingTasklist = Tasklist.builder()
                .id(tasklistId)
                .name("Nome Original")
                .description("Descrição Original")
                .completed(false)
                .userId(userId)
                .build();
    }

    @Nested
    @DisplayName("Método update")
    class UpdateMethodTests {

        @Test
        @DisplayName("Deve atualizar tasklist através do output gateway")
        void shouldUpdateTasklistThroughOutputGateway() {
            // Arrange
            Tasklist tasklistToUpdate = Tasklist.builder()
                    .name("Nome Atualizado")
                    .description("Descrição Atualizada")
                    .completed(true)
                    .build();

            Tasklist updatedTasklist = Tasklist.builder()
                    .id(tasklistId)
                    .name("Nome Atualizado")
                    .description("Descrição Atualizada")
                    .completed(true)
                    .userId(userId)
                    .build();

            when(outputGateway.update(eq(tasklistId.toString()), any(Tasklist.class)))
                    .thenReturn(updatedTasklist);

            // Act
            Tasklist result = useCase.update(tasklistId.toString(), tasklistToUpdate);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(tasklistId);
            assertThat(result.getName()).isEqualTo("Nome Atualizado");
            assertThat(result.getDescription()).isEqualTo("Descrição Atualizada");
            assertThat(result.isCompleted()).isTrue();

            verify(outputGateway).update(eq(tasklistId.toString()), any(Tasklist.class));
        }

        @Test
        @DisplayName("Deve retornar null quando output gateway retorna null")
        void shouldReturnNullWhenOutputGatewayReturnsNull() {
            // Arrange
            Tasklist tasklistToUpdate = Tasklist.builder()
                    .name("Nome")
                    .description("Descrição")
                    .build();

            when(outputGateway.update(anyString(), any(Tasklist.class)))
                    .thenReturn(null);

            // Act
            Tasklist result = useCase.update(tasklistId.toString(), tasklistToUpdate);

            // Assert
            assertThat(result).isNull();
            verify(outputGateway).update(eq(tasklistId.toString()), any(Tasklist.class));
        }
    }

    @Nested
    @DisplayName("Método findByEmail")
    class FindByEmailTests {

        @Test
        @DisplayName("Deve buscar usuário por email através do output gateway")
        void shouldFindUserByEmailThroughOutputGateway() {
            // Arrange
            when(outputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(user));

            // Act
            Optional<User> result = useCase.findByEmail("usuario@email.com");

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(userId);
            assertThat(result.get().getEmail()).isEqualTo("usuario@email.com");

            verify(outputGateway).findByEmail("usuario@email.com");
        }

        @Test
        @DisplayName("Deve retornar Optional.empty() quando usuário não existe")
        void shouldReturnEmptyWhenUserDoesNotExist() {
            // Arrange
            when(outputGateway.findByEmail("inexistente@email.com"))
                    .thenReturn(Optional.empty());

            // Act
            Optional<User> result = useCase.findByEmail("inexistente@email.com");

            // Assert
            assertThat(result).isEmpty();
            verify(outputGateway).findByEmail("inexistente@email.com");
        }
    }

    @Nested
    @DisplayName("Método findById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve buscar tasklist por ID através do output gateway")
        void shouldFindTasklistByIdThroughOutputGateway() {
            // Arrange
            when(outputGateway.findById(tasklistId.toString()))
                    .thenReturn(Optional.of(existingTasklist));

            // Act
            Optional<Tasklist> result = useCase.findById(tasklistId.toString());

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(tasklistId);
            assertThat(result.get().getName()).isEqualTo("Nome Original");

            verify(outputGateway).findById(tasklistId.toString());
        }

        @Test
        @DisplayName("Deve retornar Optional.empty() quando tasklist não existe")
        void shouldReturnEmptyWhenTasklistDoesNotExist() {
            // Arrange
            when(outputGateway.findById("inexistente"))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Tasklist> result = useCase.findById("inexistente");

            // Assert
            assertThat(result).isEmpty();
            verify(outputGateway).findById("inexistente");
        }
    }

    @Nested
    @DisplayName("Testes de Construtor")
    class ConstructorTests {

        @Test
        @DisplayName("Deve injetar corretamente as dependências")
        void shouldInjectDependenciesCorrectly() {
            // Arrange
            UpdateTasklistOutputGateway gateway = mock(UpdateTasklistOutputGateway.class);

            // Act
            UpdateTasklistUseCase useCase = new UpdateTasklistUseCase(gateway);

            // Assert
            assertThat(useCase).isNotNull();
        }
    }
}