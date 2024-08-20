package site.coach_coach.coach_coach_server.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
	private static final String ACCESS_TOKEN = "access_token";

	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");

		String accessToken = tokenProvider.getCookieValue(request, ACCESS_TOKEN);
		
		if (accessToken == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if (tokenProvider.validateAccessToken(accessToken)) {
			Authentication authentication = tokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
}
