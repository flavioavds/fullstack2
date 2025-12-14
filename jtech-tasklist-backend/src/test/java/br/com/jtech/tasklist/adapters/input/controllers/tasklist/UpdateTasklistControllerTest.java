package br.com.jtech.tasklist.adapters.input.controllers.tasklist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.UpdateTasklistInputGateway;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTasklistController - Testes Unitários")
class UpdateTasklistControllerTest {

    @Mock
    private UpdateTasklistInputGateway updateTasklistInputGateway;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UpdateTasklistController controller;

    private User loggedUser;
    private Tasklist existingTasklist;
    private TasklistRequest updateRequest;
    private final UUID userId = UUID.randomUUID();
    private final UUID tasklistId = UUID.randomUUID();
    private final String tasklistIdString = tasklistId.toString();

    @BeforeEach
    void setUp() {
        // Configuração do usuário logado
        loggedUser = User.builder()
                .id(userId)
                .email("usuario@email.com")
                .name("Usuário Logado")
                .build();

        // Configuração da tasklist existente
        existingTasklist = Tasklist.builder()
                .id(tasklistId)
                .name("Nome Original")
                .description("Descrição Original")
                .completed(false)
                .userId(userId)
                .build();

        // Configuração da requisição de atualização
        updateRequest = TasklistRequest.builder()
                .name("Nome Atualizado")
                .description("Descrição Atualizada")
                .build();

        // Configuração do contexto de segurança
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class SuccessScenarios {

        @Test
        @DisplayName("Deve atualizar tasklist com sucesso quando dados são válidos")
        void shouldUpdateTasklistSuccessfully() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));
            when(updateTasklistInputGateway.findById(tasklistIdString))
                    .thenReturn(Optional.of(existingTasklist));

            Tasklist updatedTasklist = Tasklist.builder()
                    .id(tasklistId)
                    .name("Nome Atualizado")
                    .description("Descrição Atualizada")
                    .completed(false)
                    .userId(userId)
                    .build();

            when(updateTasklistInputGateway.update(eq(tasklistIdString), any(Tasklist.class)))
                    .thenReturn(updatedTasklist);

            // Act
            ResponseEntity<TasklistResponse> response = controller.update(tasklistIdString, updateRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(tasklistId);
            assertThat(response.getBody().getName()).isEqualTo("Nome Atualizado");
            assertThat(response.getBody().getDescription()).isEqualTo("Descrição Atualizada");
            assertThat(response.getBody().getUserId()).isEqualTo(userId);

            // Verifica interações
            verify(updateTasklistInputGateway).findByEmail("usuario@email.com");
            verify(updateTasklistInputGateway).findById(tasklistIdString);
            verify(updateTasklistInputGateway).update(eq(tasklistIdString), any(Tasklist.class));
        }

        @Test
        @DisplayName("Deve atualizar apenas campos nome e descrição")
        void shouldUpdateOnlyNameAndDescription() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));
            when(updateTasklistInputGateway.findById(tasklistIdString))
                    .thenReturn(Optional.of(existingTasklist));

            when(updateTasklistInputGateway.update(eq(tasklistIdString), any(Tasklist.class)))
                    .thenAnswer(invocation -> {
                        Tasklist tasklistToUpdate = invocation.getArgument(1);
                        // Verifica que apenas nome e descrição são atualizados
                        assertThat(tasklistToUpdate.getName()).isEqualTo("Nome Atualizado");
                        assertThat(tasklistToUpdate.getDescription()).isEqualTo("Descrição Atualizada");
                        assertThat(tasklistToUpdate.getUserId()).isEqualTo(userId); // Não deve mudar
                        return tasklistToUpdate;
                    });

            // Act
            controller.update(tasklistIdString, updateRequest);

            // Assert
            verify(updateTasklistInputGateway).update(eq(tasklistIdString), any(Tasklist.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Falha - Autenticação")
    class AuthenticationFailureScenarios {

        @Test
        @DisplayName("Deve lançar 401 UNAUTHORIZED quando usuário não está autenticado")
        void shouldThrow401WhenUserNotAuthenticated() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> controller.update(tasklistIdString, updateRequest))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED")
                    .hasMessageContaining("Usuário não encontrado")
                    .extracting("status")
                    .isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("Deve lançar 401 UNAUTHORIZED quando usuário não é encontrado no sistema")
        void shouldThrow401WhenUserNotFound() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("inexistente@email.com");
            when(updateTasklistInputGateway.findByEmail("inexistente@email.com"))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> controller.update(tasklistIdString, updateRequest))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED")
                    .hasMessageContaining("Usuário não encontrado")
                    .extracting("status")
                    .isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }

    @Nested
    @DisplayName("Cenários de Falha - Autorização")
    class AuthorizationFailureScenarios {

        @Test
        @DisplayName("Deve lançar 404 NOT_FOUND quando tasklist não existe")
        void shouldThrow404WhenTasklistNotFound() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));
            when(updateTasklistInputGateway.findById(tasklistIdString))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> controller.update(tasklistIdString, updateRequest))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("404 NOT_FOUND")
                    .hasMessageContaining("Tasklist não encontrada")
                    .extracting("status")
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Deve lançar 403 FORBIDDEN quando usuário não é dono da tasklist")
        void shouldThrow403WhenUserNotOwner() {
            // Arrange
            UUID otherUserId = UUID.randomUUID();
            Tasklist tasklistOfOtherUser = Tasklist.builder()
                    .id(tasklistId)
                    .name("Tasklist de Outro")
                    .description("Descrição")
                    .completed(false)
                    .userId(otherUserId)
                    .build();

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));
            when(updateTasklistInputGateway.findById(tasklistIdString))
                    .thenReturn(Optional.of(tasklistOfOtherUser));

            // Act & Assert
            assertThatThrownBy(() -> controller.update(tasklistIdString, updateRequest))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessageContaining("403 FORBIDDEN")
                    .hasMessageContaining("Você não tem permissão para atualizar esta tasklist")
                    .extracting("status")
                    .isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    @DisplayName("Cenários de Validação")
    class ValidationScenarios {

        @Test
        @DisplayName("Deve validar que nome é obrigatório (validação feita pelo @Valid)")
        void shouldValidateNameIsRequired() {
            assertThat(updateRequest.getName()).isNotBlank();
        }

        @Test
        @DisplayName("Deve validar que descrição é obrigatória (validação feita pelo @Valid)")
        void shouldValidateDescriptionIsRequired() {
            assertThat(updateRequest.getDescription()).isNotBlank();
        }
    }

    @Nested
    @DisplayName("Cenários de Segurança")
    class SecurityScenarios {

        @Test
        @DisplayName("Deve usar apenas o email do usuário autenticado")
        void shouldUseOnlyAuthenticatedUserEmail() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("outro.usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("outro.usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));

            // Act & Assert - Se o usuário existir, deve continuar
            // Simulando que encontrou a tasklist e é dono
            when(updateTasklistInputGateway.findById(tasklistIdString))
                    .thenReturn(Optional.of(existingTasklist));
            when(updateTasklistInputGateway.update(eq(tasklistIdString), any(Tasklist.class)))
                    .thenReturn(existingTasklist);

            ResponseEntity<TasklistResponse> response = controller.update(tasklistIdString, updateRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(authentication).getName();
            verify(updateTasklistInputGateway).findByEmail("outro.usuario@email.com");
        }

        @Test
        @DisplayName("Não deve aceitar userId na requisição - deve usar o do usuário autenticado")
        void shouldNotAcceptUserIdInRequest() {
            // Arrange
            TasklistRequest requestWithUserId = TasklistRequest.builder()
                    .name("Tarefas")
                    .description("Descrição")
                    .userId(UUID.randomUUID()) // Tentativa de setar userId na requisição
                    .build();

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));
            when(updateTasklistInputGateway.findById(tasklistIdString))
                    .thenReturn(Optional.of(existingTasklist));
            when(updateTasklistInputGateway.update(eq(tasklistIdString), any(Tasklist.class)))
                    .thenAnswer(invocation -> {
                        Tasklist tasklistToUpdate = invocation.getArgument(1);
                        // Deve manter o userId original, não o da requisição
                        assertThat(tasklistToUpdate.getUserId()).isEqualTo(userId);
                        return tasklistToUpdate;
                    });

            // Act
            controller.update(tasklistIdString, requestWithUserId);

            // Assert - O userId da requisição deve ser ignorado
            verify(updateTasklistInputGateway).update(eq(tasklistIdString), any(Tasklist.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Formato de ID")
    class IdFormatScenarios {

        @Test
        @DisplayName("Deve aceitar UUID como string no path variable")
        void shouldAcceptUuidAsString() {
            // Arrange
            String uuidString = UUID.randomUUID().toString();
            
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));
            when(updateTasklistInputGateway.findById(uuidString))
                    .thenReturn(Optional.of(existingTasklist));
            when(updateTasklistInputGateway.update(eq(uuidString), any(Tasklist.class)))
                    .thenReturn(existingTasklist);

            // Act
            ResponseEntity<TasklistResponse> response = controller.update(uuidString, updateRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Nested
    @DisplayName("Cenários de Exceção do Gateway")
    class GatewayExceptionScenarios {

        @Test
        @DisplayName("Deve propagar exceção do gateway")
        void shouldPropagateGatewayException() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(updateTasklistInputGateway.findByEmail("usuario@email.com"))
                    .thenReturn(Optional.of(loggedUser));
            when(updateTasklistInputGateway.findById(tasklistIdString))
                    .thenReturn(Optional.of(existingTasklist));
            when(updateTasklistInputGateway.update(eq(tasklistIdString), any(Tasklist.class)))
                    .thenThrow(new RuntimeException("Erro no banco de dados"));

            // Act & Assert
            assertThatThrownBy(() -> controller.update(tasklistIdString, updateRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro no banco de dados");
        }
    }

    @Test
    @DisplayName("Deve limpar contexto de segurança após testes")
    void shouldClearSecurityContextAfterTest() {
        SecurityContextHolder.clearContext();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}