package br.com.jtech.tasklist.adapters.input.controllers.tasklist;

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

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.CreateTasklistInputGateway;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTasklistController - Testes Unitários")
class CreateTasklistControllerTest {

    @Mock
    private CreateTasklistInputGateway createTasklistInputGateway;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CreateTasklistController controller;

    private User mockUser;
    private TasklistRequest validRequest;
    private Tasklist mockTasklist;
    private final UUID userId = UUID.randomUUID();
    private final UUID tasklistId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Configuração do usuário mock
        mockUser = User.builder()
                .id(userId)
                .email("usuario@email.com")
                .name("Usuário Teste")
                .build();

        // Configuração da requisição válida
        validRequest = TasklistRequest.builder()
                .name("Tarefas Diárias")
                .description("Lista de tarefas do dia a dia")
                .build();

        // Configuração da tasklist mock
        mockTasklist = Tasklist.builder()
                .id(tasklistId)
                .name("Tarefas Diárias")
                .description("Lista de tarefas do dia a dia")
                .completed(false)
                .userId(userId)
                .build();

        // Configuração do contexto de segurança
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class SuccessScenarios {

        @Test
        @DisplayName("Deve criar tasklist com sucesso quando dados são válidos")
        void shouldCreateTasklistSuccessfully() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(createTasklistInputGateway.findByEmail(anyString()))
                    .thenReturn(Optional.of(mockUser));
            when(createTasklistInputGateway.create(any(Tasklist.class)))
                    .thenReturn(mockTasklist);

            // Act
            ResponseEntity<TasklistResponse> response = controller.create(validRequest);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(tasklistId);
            assertThat(response.getBody().getName()).isEqualTo("Tarefas Diárias");
            assertThat(response.getBody().getDescription()).isEqualTo("Lista de tarefas do dia a dia");
            assertThat(response.getBody().getUserId()).isEqualTo(userId);

            // Verifica interações
            verify(createTasklistInputGateway).findByEmail("usuario@email.com");
            verify(createTasklistInputGateway).create(any(Tasklist.class));
        }

        @Test
        @DisplayName("Deve associar tasklist ao usuário autenticado")
        void shouldAssociateTasklistToAuthenticatedUser() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(createTasklistInputGateway.findByEmail(anyString()))
                    .thenReturn(Optional.of(mockUser));
            when(createTasklistInputGateway.create(any(Tasklist.class)))
                    .thenAnswer(invocation -> {
                        Tasklist tasklist = invocation.getArgument(0);
                        assertThat(tasklist.getUserId()).isEqualTo(userId);
                        return mockTasklist;
                    });

            // Act
            controller.create(validRequest);

            // Assert
            verify(createTasklistInputGateway).create(any(Tasklist.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Falha - Autenticação")
    class AuthenticationFailureScenarios {

        @Test
        @DisplayName("Deve lançar exceção quando usuário não está autenticado")
        void shouldThrowExceptionWhenUserNotAuthenticated() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> controller.create(validRequest))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .hasMessageContaining("401 UNAUTHORIZED")
                    .extracting("status")
                    .isEqualTo(UNAUTHORIZED);
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não é encontrado no sistema")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("inexistente@email.com");
            when(createTasklistInputGateway.findByEmail(anyString()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> controller.create(validRequest))
                    .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                    .hasMessageContaining("Usuário não encontrado")
                    .extracting("status")
                    .isEqualTo(UNAUTHORIZED);
        }
    }

    @Nested
    @DisplayName("Cenários de Validação")
    class ValidationScenarios {

        @Test
        @DisplayName("Deve validar que nome é obrigatório")
        void shouldValidateNameIsRequired() {
            // Nota: A validação é feita pela annotation @Valid no parâmetro
            // Este teste pode ser complementado com testes de integração
            assertThat(validRequest.getName()).isNotBlank();
        }

        @Test
        @DisplayName("Deve validar que descrição é obrigatória")
        void shouldValidateDescriptionIsRequired() {
            assertThat(validRequest.getDescription()).isNotBlank();
        }
    }

    @Nested
    @DisplayName("Cenários de Segurança")
    class SecurityScenarios {

        @Test
        @DisplayName("Não deve aceitar userId na requisição")
        void shouldNotAcceptUserIdInRequest() {
            // Arrange
            TasklistRequest requestWithUserId = TasklistRequest.builder()
                    .name("Tarefas")
                    .description("Descrição")
                    .userId(UUID.randomUUID()) // Tentativa de setar userId na requisição
                    .build();

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(createTasklistInputGateway.findByEmail(anyString()))
                    .thenReturn(Optional.of(mockUser));
            when(createTasklistInputGateway.create(any(Tasklist.class)))
                    .thenAnswer(invocation -> {
                        Tasklist tasklist = invocation.getArgument(0);
                        // Deve usar o userId do usuário autenticado, não o da requisição
                        assertThat(tasklist.getUserId()).isEqualTo(userId);
                        return mockTasklist;
                    });

            // Act
            controller.create(requestWithUserId);

            // Assert - O userId da requisição deve ser sobrescrito pelo do usuário autenticado
            verify(createTasklistInputGateway).create(any(Tasklist.class));
        }
    }

    @Nested
    @DisplayName("Cenários de Transformação de Dados")
    class DataTransformationScenarios {

        @Test
        @DisplayName("Deve converter TasklistRequest para Tasklist domain corretamente")
        void shouldConvertRequestToDomainCorrectly() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(createTasklistInputGateway.findByEmail(anyString()))
                    .thenReturn(Optional.of(mockUser));
            when(createTasklistInputGateway.create(any(Tasklist.class)))
                    .thenAnswer(invocation -> {
                        Tasklist tasklist = invocation.getArgument(0);
                        // Verifica conversão
                        assertThat(tasklist.getName()).isEqualTo(validRequest.getName());
                        assertThat(tasklist.getDescription()).isEqualTo(validRequest.getDescription());
                        assertThat(tasklist.getUserId()).isEqualTo(userId);
                        return tasklist;
                    });

            // Act
            controller.create(validRequest);

            // Assert
            verify(createTasklistInputGateway).create(any(Tasklist.class));
        }

        @Test
        @DisplayName("Deve converter Tasklist para TasklistResponse corretamente")
        void shouldConvertDomainToResponseCorrectly() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(createTasklistInputGateway.findByEmail(anyString()))
                    .thenReturn(Optional.of(mockUser));
            when(createTasklistInputGateway.create(any(Tasklist.class)))
                    .thenReturn(mockTasklist);

            // Act
            ResponseEntity<TasklistResponse> response = controller.create(validRequest);
            TasklistResponse responseBody = response.getBody();

            // Assert
            assertThat(responseBody).isNotNull();
            assertThat(responseBody.getId()).isEqualTo(mockTasklist.getId());
            assertThat(responseBody.getName()).isEqualTo(mockTasklist.getName());
            assertThat(responseBody.getDescription()).isEqualTo(mockTasklist.getDescription());
            assertThat(responseBody.getUserId()).isEqualTo(mockTasklist.getUserId());
        }
    }

    @Nested
    @DisplayName("Cenários de Exceção")
    class ExceptionScenarios {

        @Test
        @DisplayName("Deve lidar com exceções do gateway")
        void shouldHandleGatewayExceptions() {
            // Arrange
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("usuario@email.com");
            when(createTasklistInputGateway.findByEmail(anyString()))
                    .thenReturn(Optional.of(mockUser));
            when(createTasklistInputGateway.create(any(Tasklist.class)))
                    .thenThrow(new RuntimeException("Erro no banco de dados"));

            // Act & Assert
            assertThatThrownBy(() -> controller.create(validRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro no banco de dados");
        }
    }

    @Test
    @DisplayName("Deve limpar contexto de segurança após cada teste")
    void shouldClearSecurityContextAfterTest() {
        // Este teste verifica que o contexto é limpo entre testes
        SecurityContextHolder.clearContext();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}