package site.coach_coach.coach_coach_server.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.TokenService;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.utils.AuthenticationUtil;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final TokenService tokenService;
	private final TokenProvider tokenProvider;
	private final AuthenticationUtil authenticationUtil;

	@PostMapping("/v1/auth/signup")
	public ResponseEntity<Void> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
		userService.signup(signUpRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/v1/auth/login")
	public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
		User user = userService.validateUser(loginRequest);
		TokenDto tokenDto = userService.createJwt(user);

		response.addCookie(tokenProvider.createCookie("access_token", tokenDto.accessToken()));
		response.addCookie(tokenProvider.createCookie("refresh_token", tokenDto.refreshToken()));
		tokenService.createRefreshToken(user, tokenDto.refreshToken(), tokenDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/v1/auth/logout")
	public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
		HttpServletRequest request, HttpServletResponse response) {
		Long userId = userDetails.getUserId();

		String refreshToken = tokenProvider.getCookieValue(request, "refresh_token");
		tokenService.deleteRefreshToken(userId, refreshToken);

		tokenProvider.clearCookie(response, "access_token");
		tokenProvider.clearCookie(response, "refresh_token");

		SecurityContextHolder.clearContext();

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/v1/auth/reissue")
	public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = tokenProvider.getCookieValue(request, "refresh_token");

		String newAccessToken = tokenService.reissueAccessToken(refreshToken);
		tokenProvider.clearCookie(response, "access_token");

		Cookie newAccessTokenCookie = tokenProvider.createCookie("access_token", newAccessToken);
		response.addCookie(newAccessTokenCookie);

		return ResponseEntity.noContent().build();
	}
}
