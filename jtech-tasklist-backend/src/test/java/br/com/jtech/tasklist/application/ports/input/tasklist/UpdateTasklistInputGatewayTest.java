package br.com.jtech.tasklist.application.ports.input.tasklist;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.jtech.tasklist.application.core.domains.Tasklist;

@DisplayName("UpdateTasklistInputGateway - Testes de Interface")
class UpdateTasklistInputGatewayTest {

    @Test
    @DisplayName("Deve ter método update definido")
    void shouldHaveUpdateMethod() throws NoSuchMethodException {
        // Arrange & Act
        Method method = UpdateTasklistInputGateway.class.getMethod("update", String.class, Tasklist.class);

        // Assert
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(Tasklist.class);
        assertThat(method.getParameterCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve ter método findByEmail definido")
    void shouldHaveFindByEmailMethod() throws NoSuchMethodException {
        // Arrange & Act
        Method method = UpdateTasklistInputGateway.class.getMethod("findByEmail", String.class);

        // Assert
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(Optional.class);
        assertThat(method.getParameterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve ter método findById definido")
    void shouldHaveFindByIdMethod() throws NoSuchMethodException {
        // Arrange & Act
        Method method = UpdateTasklistInputGateway.class.getMethod("findById", String.class);

        // Assert
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(Optional.class);
        assertThat(method.getParameterCount()).isEqualTo(1);
    }
}