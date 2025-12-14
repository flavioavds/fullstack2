package br.com.jtech.tasklist.adapters.input.controllers.tasklist;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("Validação de Beans - TasklistRequest e TasklistResponse")
class BeanValidationTest {

    @BeforeAll
    static void setUp() {
        // Configurar BeanMatchers para ignorar alguns padrões
        BeanMatchers.registerValueGenerator(() -> java.util.UUID.randomUUID().toString(), String.class);
    }

    @Test
    @DisplayName("TasklistRequest deve ser um bean válido")
    void tasklistRequestShouldBeValidBean() {
        assertThat(br.com.jtech.tasklist.adapters.input.protocols.TasklistRequest.class,
                allOf(
                        hasValidBeanConstructor(),
                        hasValidGettersAndSetters(),
                        hasValidBeanHashCode(),
                        hasValidBeanEquals(),
                        hasValidBeanToString()
                ));
    }

    @Test
    @DisplayName("TasklistResponse deve ser um bean válido")
    void tasklistResponseShouldBeValidBean() {
        assertThat(br.com.jtech.tasklist.adapters.input.protocols.TasklistResponse.class,
                allOf(
                        hasValidBeanConstructor(),
                        hasValidGettersAndSetters(),
                        hasValidBeanHashCode(),
                        hasValidBeanEquals(),
                        hasValidBeanToString()
                ));
    }
}