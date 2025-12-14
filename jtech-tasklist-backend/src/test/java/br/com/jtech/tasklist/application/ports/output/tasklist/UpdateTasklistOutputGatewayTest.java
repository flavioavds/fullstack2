package br.com.jtech.tasklist.application.ports.output.tasklist;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.ports.input.tasklist.UpdateTasklistInputGateway;

@DisplayName("UpdateTasklistOutputGateway - Testes de Interface")
class UpdateTasklistOutputGatewayTest {

    @Test
    @DisplayName("Deve ter método update definido")
    void shouldHaveUpdateMethod() throws NoSuchMethodException {
        // Arrange & Act
        Method method = UpdateTasklistOutputGateway.class.getMethod("update", String.class, Tasklist.class);

        // Assert
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(Tasklist.class);
        assertThat(method.getParameterCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve ter método findByEmail definido")
    void shouldHaveFindByEmailMethod() throws NoSuchMethodException {
        // Arrange & Act
        Method method = UpdateTasklistOutputGateway.class.getMethod("findByEmail", String.class);

        // Assert
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(Optional.class);
        assertThat(method.getParameterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve ter método findById definido")
    void shouldHaveFindByIdMethod() throws NoSuchMethodException {
        // Arrange & Act
        Method method = UpdateTasklistOutputGateway.class.getMethod("findById", String.class);

        // Assert
        assertThat(method).isNotNull();
        assertThat(method.getReturnType()).isEqualTo(Optional.class);
        assertThat(method.getParameterCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve ser interface compatível com InputGateway")
    void shouldBeCompatibleWithInputGateway() {
        // Arrange
        Class<?> inputGateway = UpdateTasklistInputGateway.class;
        Class<?> outputGateway = UpdateTasklistOutputGateway.class;

        // Assert
        assertThat(inputGateway.getMethods()).hasSize(outputGateway.getMethods().length);
        
        for (Method inputMethod : inputGateway.getMethods()) {
            boolean found = false;
            for (Method outputMethod : outputGateway.getMethods()) {
                if (inputMethod.getName().equals(outputMethod.getName()) &&
                    inputMethod.getReturnType().equals(outputMethod.getReturnType()) &&
                    inputMethod.getParameterCount() == outputMethod.getParameterCount()) {
                    found = true;
                    break;
                }
            }
            assertThat(found).isTrue()
                .withFailMessage("Método %s não encontrado em UpdateTasklistOutputGateway", inputMethod.getName());
        }
    }
}