package br.com.jtech.tasklist.adapters.input.controllers.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.jtech.tasklist.adapters.input.protocols.UserRequest;
import br.com.jtech.tasklist.adapters.input.protocols.UserResponse;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.auth.RegisterUserInputGateway;

public class RegisterAuthUserControllerTest {

    @Mock
    private RegisterUserInputGateway registerUserInputGateway;

    @InjectMocks
    private RegisterAuthUserController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        UserRequest request = UserRequest.builder()
                .name("Flavio Souza")
                .email("flavio.souza@email.com")
                .password("123456")
                .build();

//        User domainUser = User.builder()
//                .name(request.getName())
//                .email(request.getEmail())
//                .password(request.getPassword())
//                .build();

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        when(registerUserInputGateway.register(any(User.class))).thenReturn(savedUser);

        ResponseEntity<UserResponse> responseEntity = controller.register(request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(savedUser.getId());
        assertThat(responseEntity.getBody().getName()).isEqualTo(savedUser.getName());
        assertThat(responseEntity.getBody().getEmail()).isEqualTo(savedUser.getEmail());

        verify(registerUserInputGateway, times(1)).register(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        UserRequest request = UserRequest.builder()
                .name("Flavio Souza")
                .email("flavio.souza@email.com")
                .password("123456")
                .build();

        when(registerUserInputGateway.register(any(User.class)))
                .thenThrow(new RuntimeException("Usuário já existente"));

        try {
            controller.register(request);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Usuário já existente");
        }

        verify(registerUserInputGateway, times(1)).register(any(User.class));
    }
    
    @Test
    void testRegisterWhereUser_Success() {
        UserRequest request = UserRequest.builder()
                .name("Flavio Souza")
                .email("flavio.souza@email.com")
                .password("123456")
                .build();

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        when(registerUserInputGateway.register(any(User.class))).thenReturn(savedUser);

        ResponseEntity<UserResponse> responseEntity = controller.register(request);

        System.out.println("Teste de registro de usuário executado com sucesso!");
        System.out.println("ID do usuário criado: " + responseEntity.getBody().getId());
        System.out.println("Nome do usuário: " + responseEntity.getBody().getName());
        System.out.println("Email do usuário: " + responseEntity.getBody().getEmail());

        assertThat(responseEntity.getStatusCode())
                .withFailMessage("O status deveria ser 201 CREATED")
                .isEqualTo(HttpStatus.CREATED);

        assertThat(responseEntity.getBody())
                .withFailMessage("O corpo da resposta não deve ser nulo")
                .isNotNull();

        verify(registerUserInputGateway, times(1)).register(any(User.class));
    }

}