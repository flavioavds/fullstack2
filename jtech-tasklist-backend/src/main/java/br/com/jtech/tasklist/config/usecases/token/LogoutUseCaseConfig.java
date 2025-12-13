package br.com.jtech.tasklist.config.usecases.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.jtech.tasklist.application.core.usecases.token.LogoutUseCase;
import br.com.jtech.tasklist.application.ports.output.token.LogoutOutputGateway;
import br.com.jtech.tasklist.config.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LogoutUseCaseConfig {
	
	private final LogoutOutputGateway logoutOutputGateway;
    private final JwtService jwtService;
	
    @Bean
	public LogoutUseCase logoutUseCase() {
		return new LogoutUseCase(logoutOutputGateway, jwtService);
	}

}
