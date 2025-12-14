package br.com.jtech.tasklist.adapters.input.controllers.tasklist;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.tasklist.CreateTasklistInputGateway;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("CreateTasklistController - Testes de Integração")
class CreateTasklistControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
	@MockBean
    private CreateTasklistInputGateway createTasklistInputGateway;

    private User mockUser;
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

        // Configuração da tasklist mock
        mockTasklist = Tasklist.builder()
                .id(tasklistId)
                .name("Tarefas Diárias")
                .description("Lista de tarefas do dia a dia")
                .completed(false)
                .userId(userId)
                .build();
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve criar tasklist com sucesso")
    void shouldCreateTasklistSuccessfully() throws Exception {
        // Arrange
        TasklistRequest request = TasklistRequest.builder()
                .name("Tarefas Diárias")
                .description("Lista de tarefas do dia a dia")
                .build();

        when(createTasklistInputGateway.findByEmail(anyString()))
                .thenReturn(Optional.of(mockUser));
        when(createTasklistInputGateway.create(any(Tasklist.class)))
                .thenReturn(mockTasklist);

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(tasklistId.toString())))
                .andExpect(jsonPath("$.name", is("Tarefas Diárias")))
                .andExpect(jsonPath("$.description", is("Lista de tarefas do dia a dia")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.userId", is(userId.toString())));
    }

    @Test
    @DisplayName("POST /api/v1/tasklists - Deve retornar 401 quando não autenticado")
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        // Arrange
        TasklistRequest request = TasklistRequest.builder()
                .name("Tarefas Diárias")
                .description("Lista de tarefas do dia a dia")
                .build();

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve retornar 400 quando nome está vazio")
    void shouldReturn400WhenNameIsEmpty() throws Exception {
        // Arrange
        TasklistRequest request = TasklistRequest.builder()
                .name("")
                .description("Lista de tarefas do dia a dia")
                .build();

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve retornar 400 quando descrição está vazia")
    void shouldReturn400WhenDescriptionIsEmpty() throws Exception {
        // Arrange
        TasklistRequest request = TasklistRequest.builder()
                .name("Tarefas Diárias")
                .description("")
                .build();

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve retornar 400 quando nome é nulo")
    void shouldReturn400WhenNameIsNull() throws Exception {
        // Arrange
        String requestJson = """
            {
                "description": "Lista de tarefas do dia a dia"
            }
            """;

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve retornar 400 quando descrição é nula")
    void shouldReturn400WhenDescriptionIsNull() throws Exception {
        // Arrange
        String requestJson = """
            {
                "name": "Tarefas Diárias"
            }
            """;

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve retornar 400 quando nome tem apenas espaços")
    void shouldReturn400WhenNameHasOnlySpaces() throws Exception {
        // Arrange
        TasklistRequest request = TasklistRequest.builder()
                .name("   ")
                .description("Lista de tarefas do dia a dia")
                .build();

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve ignorar campos extras na requisição")
    void shouldIgnoreExtraFieldsInRequest() throws Exception {
        // Arrange
        String requestJson = """
            {
                "name": "Tarefas Diárias",
                "description": "Lista de tarefas do dia a dia",
                "extraField": "valor extra",
                "completed": true,
                "userId": "11111111-1111-1111-1111-111111111111"
            }
            """;

        when(createTasklistInputGateway.findByEmail(anyString()))
                .thenReturn(Optional.of(mockUser));
        when(createTasklistInputGateway.create(any(Tasklist.class)))
                .thenReturn(mockTasklist);

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // Assert - Deve criar normalmente ignorando campos extras
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.completed", is(false))) // Não deve aceitar completed na criação
                .andExpect(jsonPath("$.userId", is(userId.toString()))); // Deve usar userId do usuário autenticado
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve retornar 500 quando ocorre erro interno")
    void shouldReturn500WhenInternalErrorOccurs() throws Exception {
        // Arrange
        TasklistRequest request = TasklistRequest.builder()
                .name("Tarefas Diárias")
                .description("Lista de tarefas do dia a dia")
                .build();

        when(createTasklistInputGateway.findByEmail(anyString()))
                .thenReturn(Optional.of(mockUser));
        when(createTasklistInputGateway.create(any(Tasklist.class)))
                .thenThrow(new RuntimeException("Erro no banco de dados"));

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(username = "usuario@email.com")
    @DisplayName("POST /api/v1/tasklists - Deve validar tamanho máximo do nome")
    void shouldValidateMaxNameLength() throws Exception {
        // Arrange
        String longName = "A".repeat(256); // Nome muito longo
        TasklistRequest request = TasklistRequest.builder()
                .name(longName)
                .description("Lista de tarefas do dia a dia")
                .build();

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/tasklists")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert - Se houver validação de tamanho, deve retornar 400
        // Se não houver, este teste pode ser ajustado
        result.andExpect(status().is4xxClientError());
    }
    
}