package site.coach_coach.coach_coach_server.auth.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");

		try {
			filterChain.doFilter(request, response);
		} catch (JwtException ex) {
			String message = ex.getMessage();

			if (ErrorMessage.EXPIRED_TOKEN.getMessage().equals(message)) {
				setErrorResponse(response, ErrorMessage.EXPIRED_TOKEN);
			} else if (ErrorMessage.NOT_FOUND_TOKEN.getMessage().equals(message)) {
				setErrorResponse(response, ErrorMessage.NOT_FOUND_TOKEN);
			} else {
				setErrorResponse(response, ErrorMessage.INVALID_TOKEN);
			}
		}
	}

	private void setErrorResponse(HttpServletResponse response, ErrorMessage message)
		throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("message", message.getMessage());

		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(jsonResponse);
	}
}
