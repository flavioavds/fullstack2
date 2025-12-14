package br.com.jtech.tasklist.adapters.output.tasklist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.jtech.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTasklistAdapter - Testes Unitários")
class UpdateTasklistAdapterTest {

    @Mock
    private TasklistRepository tasklistRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateTasklistAdapter adapter;

    private TasklistEntity existingEntity;
    private UserEntity userEntity;
    private final UUID tasklistId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(userId)
                .email("usuario@email.com")
                .name("Usuário")
                .build();

        existingEntity = TasklistEntity.builder()
                .id(tasklistId)
                .name("Nome Original")
                .desciption("Descrição Original")
                .completed(false)
                .user(userEntity)
                .createdAt(now.minusDays(1))
                .updatedAt(now.minusDays(1))
                .build();
    }

    @Nested
    @DisplayName("Método update")
    class UpdateMethodTests {

        @Test
        @DisplayName("Deve atualizar tasklist no repositório")
        void shouldUpdateTasklistInRepository() {
            // Arrange
            Tasklist tasklistToUpdate = Tasklist.builder()
                    .name("Nome Atualizado")
                    .description("Descrição Atualizada")
                    .completed(true)
                    .build();

            TasklistEntity updatedEntity = TasklistEntity.builder()
                    .id(tasklistId)
                    .name("Nome Atualizado")
                    .desciption("Descrição Atualizada")
                    .completed(true)
                    .user(userEntity)
                    .createdAt(now.minusDays(1))
                    .updatedAt(now)
                    .build();

            when(tasklistRepository.findById(tasklistId))
                    .thenReturn(Optional.of(existingEntity));
            when(tasklistRepository.save(any(TasklistEntity.class)))
                    .thenReturn(updatedEntity);

            // Act
            Tasklist result = adapter.update(tasklistId.toString(), tasklistToUpdate);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(tasklistId);
            assertThat(result.getName()).isEqualTo("Nome Atualizado");
            assertThat(result.getDescription()).isEqualTo("Descrição Atualizada");
            assertThat(result.isCompleted()).isTrue();
            assertThat(result.getUserId()).isEqualTo(userId);

            verify(tasklistRepository).findById(tasklistId);
            verify(tasklistRepository).save(any(TasklistEntity.class));
        }

        @Test
        @DisplayName("Deve retornar null quando tasklist não existe")
        void shouldReturnNullWhenTasklistNotFound() {
            // Arrange
            Tasklist tasklistToUpdate = Tasklist.builder()
                    .name("Nome")
                    .description("Descrição")
                    .build();

            when(tasklistRepository.findById(tasklistId))
                    .thenReturn(Optional.empty());

            // Act
            Tasklist result = adapter.update(tasklistId.toString(), tasklistToUpdate);

            // Assert
            assertThat(result).isNull();
            verify(tasklistRepository).findById(tasklistId);
            verify(tasklistRepository, never()).save(any(TasklistEntity.class));
        }

        @Test
        @DisplayName("Deve atualizar apenas campos permitidos mantendo outros inalterados")
        void shouldUpdateOnlyAllowedFieldsKeepingOthersUnchanged() {
            // Arrange
            Tasklist tasklistToUpdate = Tasklist.builder()
                    .name("Novo Nome")
                    .description("Nova Descrição")
                    .completed(true)
                    .build();

            when(tasklistRepository.findById(tasklistId))
                    .thenReturn(Optional.of(existingEntity));
            when(tasklistRepository.save(any(TasklistEntity.class)))
                    .thenAnswer(invocation -> {
                        TasklistEntity entity = invocation.getArgument(0);
                        // Verifica que campos são atualizados
                        assertThat(entity.getName()).isEqualTo("Novo Nome");
                        assertThat(entity.getDesciption()).isEqualTo("Nova Descrição");
                        assertThat(entity.isCompleted()).isTrue();
                        // Verifica que campos não devem mudar
                        assertThat(entity.getId()).isEqualTo(tasklistId);
                        assertThat(entity.getUser()).isEqualTo(userEntity);
                        assertThat(entity.getCreatedAt()).isEqualTo(now.minusDays(1));
                        return entity;
                    });

            // Act
            adapter.update(tasklistId.toString(), tasklistToUpdate);

            // Assert
            verify(tasklistRepository).save(any(TasklistEntity.class));
        }
    }

    @Nested
    @DisplayName("Método findByEmail")
    class FindByEmailTests {

        @Test
        @DisplayName("Deve buscar usuário por email")
        void shouldFindUserByEmail() {
            // Arrange
            when(userRepository.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(userEntity));

            // Act
            Optional<User> result = adapter.findByEmail("usuario@email.com");

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(userId);
            assertThat(result.get().getEmail()).isEqualTo("usuario@email.com");

            verify(userRepository).findByEmail("usuario@email.com");
        }

        @Test
        @DisplayName("Deve retornar Optional.empty() quando usuário não existe")
        void shouldReturnEmptyWhenUserNotFound() {
            // Arrange
            when(userRepository.findByEmail("inexistente@email.com"))
                    .thenReturn(Optional.empty());

            // Act
            Optional<User> result = adapter.findByEmail("inexistente@email.com");

            // Assert
            assertThat(result).isEmpty();
            verify(userRepository).findByEmail("inexistente@email.com");
        }
    }

    @Nested
    @DisplayName("Método findById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve buscar tasklist por ID")
        void shouldFindTasklistById() {
            // Arrange
            when(tasklistRepository.findById(tasklistId))
                    .thenReturn(Optional.of(existingEntity));

            // Act
            Optional<Tasklist> result = adapter.findById(tasklistId.toString());

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(tasklistId);
            assertThat(result.get().getName()).isEqualTo("Nome Original");
            assertThat(result.get().getDescription()).isEqualTo("Descrição Original");
            assertThat(result.get().getUserId()).isEqualTo(userId);

            verify(tasklistRepository).findById(tasklistId);
        }

        @Test
        @DisplayName("Deve retornar Optional.empty() quando tasklist não existe")
        void shouldReturnEmptyWhenTasklistNotFound() {
            // Arrange
            when(tasklistRepository.findById(tasklistId))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Tasklist> result = adapter.findById(tasklistId.toString());

            // Assert
            assertThat(result).isEmpty();
            verify(tasklistRepository).findById(tasklistId);
        }

        @Test
        @DisplayName("Deve lidar com ID inválido")
        void shouldHandleInvalidId() {
            // Act & Assert
            // UUID.fromString lançará IllegalArgumentException para ID inválido
            assertThatThrownBy(() -> adapter.findById("id-invalido"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}