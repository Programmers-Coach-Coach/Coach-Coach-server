package site.coach_coach.coach_coach_server.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExceptionHandlerConfig implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {
	@Override
	public void customize(ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer) {
		httpSecurityExceptionHandlingConfigurer
			.authenticationEntryPoint(this::handleAuthenticationException)
			.accessDeniedHandler(this::handleAccessDeniedException);
	}

	private void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}

	private void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
