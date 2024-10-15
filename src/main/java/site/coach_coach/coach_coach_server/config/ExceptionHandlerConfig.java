package site.coach_coach.coach_coach_server.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.response.ErrorResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerConfig implements Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {
	private final ObjectMapper objectMapper;

	@Override
	public void customize(ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer) {
		httpSecurityExceptionHandlingConfigurer
			.authenticationEntryPoint(this::handleAuthenticationException)
			.accessDeniedHandler(this::handleAccessDeniedException);
	}

	private void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		log.error("Unauthorized request - Method: {}, URI: {}, Error: {}",
			request.getMethod(),
			request.getRequestURI(),
			authException.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		ErrorResponse errorResponse = new ErrorResponse(
			HttpServletResponse.SC_UNAUTHORIZED,
			ErrorMessage.NOT_FOUND_TOKEN
		);

		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(jsonResponse);
	}

	private void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
