package site.coach_coach.coach_coach_server.auth.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.TokenService;
import site.coach_coach.coach_coach_server.auth.oauth.dto.CustomOAuth2User;
import site.coach_coach.coach_coach_server.common.domain.UserRole;
import site.coach_coach.coach_coach_server.user.domain.User;

@Component
@RequiredArgsConstructor
public class CustomOAuth2Handler extends SimpleUrlAuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;
	private final TokenService tokenService;
	@Value("${redirect.url}")
	private String redirectUrl;
	private static final String[] DOMAINS = {"localhost", ".coach-coach.site"};

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();

		User user = customUserDetails.getUser();

		TokenDto tokenDto = tokenProvider.generateJwt(user);

		setCookies(response, "access_token", tokenDto.accessToken());
		setCookies(response, "refresh_token", tokenDto.refreshToken());
		tokenService.createRefreshToken(user, tokenDto.refreshToken(), tokenDto);

		String role = tokenProvider.getRoleFromJwt(tokenDto.accessToken());
		if (role.equals(UserRole.ROLE_GUEST.name())) {
			response.sendRedirect(redirectUrl + "/nickname");
		} else {
			response.sendRedirect(redirectUrl);
		}
	}

	private void setCookies(HttpServletResponse response, String tokenName, String tokenValue) {
		for (String domain : DOMAINS) {
			String cookie = tokenProvider.createCookie(tokenName, tokenValue, domain).toString();
			response.addHeader("Set-Cookie", cookie);
		}
	}
}
