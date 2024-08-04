package site.coach_coach.coach_coach_server.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	private static final String TOKEN_HEADER = "Authorization";
	private static final String TOKEN_PREFIX = "Bearer ";

	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");

		String authorizationHeader = request.getHeader(TOKEN_HEADER);

		String requestUri = request.getRequestURI();
		if (requestUri.equals("/auth/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (authorizationHeader == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!authorizationHeader.startsWith(TOKEN_PREFIX)) {
			throw new JwtException(ErrorMessage.INVALID_TOKEN.getMessage());
		}

		String accessToken = authorizationHeader.split(" ")[1];
		if (tokenProvider.validateAccessToken(accessToken)) {
			Authentication authentication = tokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
}
