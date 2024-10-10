package site.coach_coach.coach_coach_server.auth.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.TokenService;
import site.coach_coach.coach_coach_server.auth.oauth.dto.CustomOAuth2User;
import site.coach_coach.coach_coach_server.user.domain.User;

@Component
@RequiredArgsConstructor
public class CustomOAuth2Handler extends SimpleUrlAuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;
	private final TokenService tokenService;
	@Value("${redirect.url}")
	private String redirectUrl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User customUserDetails = (CustomOAuth2User)authentication.getPrincipal();

		User user = customUserDetails.getUser();

		TokenDto tokenDto = tokenProvider.generateJwt(user);

		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("access_token", tokenDto.accessToken(), "localhost").toString());
		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("refresh_token", tokenDto.refreshToken(), "localhost").toString());

		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("access_token", tokenDto.accessToken(), ".coach-coach.site").toString());
		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("refresh_token", tokenDto.refreshToken(), ".coach-coach.site").toString());
		tokenService.createRefreshToken(user, tokenDto.refreshToken(), tokenDto);

		response.sendRedirect(redirectUrl);
	}
}
