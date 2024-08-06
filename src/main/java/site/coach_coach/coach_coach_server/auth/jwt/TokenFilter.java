package site.coach_coach.coach_coach_server.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
	private static final String ACCESS_TOKEN = "access_token";
	private static final String REFRESH_TOKEN = "refresh_token";

	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");

		String requestUri = request.getRequestURI();
		if (requestUri.equals("/api/v1/auth/login") || requestUri.equals("/api/v1/auth/signup")) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = getCookieValue(request, ACCESS_TOKEN);
		String refreshToken = getCookieValue(request, REFRESH_TOKEN);
		if (accessToken != null) {
			if (tokenProvider.validateAccessToken(accessToken)) {
				Authentication authentication = tokenProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else if (!tokenProvider.validateAccessToken(accessToken) && refreshToken != null) {
				boolean validRefreshToken = tokenProvider.validateRefreshToken(refreshToken);
				boolean isRefreshToken = tokenProvider.existsRefreshToken(refreshToken);

				if (validRefreshToken && isRefreshToken) {
					String newAccessToken = tokenProvider.regenerateAccessToken(refreshToken);

					Cookie oldAccessTokenCookie = new Cookie(ACCESS_TOKEN, null);
					oldAccessTokenCookie.setHttpOnly(true);
					oldAccessTokenCookie.setPath("/");
					oldAccessTokenCookie.setMaxAge(0);
					response.addCookie(oldAccessTokenCookie);
					
					Cookie newAccessTokenCookie = new Cookie(ACCESS_TOKEN, newAccessToken);
					response.addCookie(newAccessTokenCookie);

					Authentication authentication = tokenProvider.getAuthentication(newAccessToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	private String getCookieValue(HttpServletRequest request, String type) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (type.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
