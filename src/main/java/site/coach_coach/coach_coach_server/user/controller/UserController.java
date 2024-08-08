package site.coach_coach.coach_coach_server.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.RefreshTokenService;
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
	private final RefreshTokenService refreshTokenService;
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
		refreshTokenService.createRefreshToken(user, tokenDto.refreshToken(), tokenDto);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/v1/auth")
	public ResponseEntity<Map<String, Boolean>> auth() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAuthenticated = authentication != null && authenticationUtil.isAuthenticated(authentication);

		Map<String, Boolean> response = new HashMap<>();
		response.put("isLogin", isAuthenticated);

		return ResponseEntity.ok(response);
	}
}
