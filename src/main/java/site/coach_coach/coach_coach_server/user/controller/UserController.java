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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.TokenService;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.common.utils.AuthenticationUtil;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.service.UserService;
import site.coach_coach.coach_coach_server.user.validation.Nickname;
import site.coach_coach.coach_coach_server.user.validation.Password;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final TokenService tokenService;
	private final TokenProvider tokenProvider;
	private final AuthenticationUtil authenticationUtil;

	@PostMapping("/v1/auth/signup")
	public ResponseEntity<SuccessResponse> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
		userService.signup(signUpRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(), "회원가입 성공"));
	}

	@PostMapping("/v1/auth/login")
	public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest,
		HttpServletResponse response) {
		User user = userService.validateUser(loginRequest);
		TokenDto tokenDto = userService.createJwt(user);

		response.addCookie(tokenProvider.createCookie("access_token", tokenDto.accessToken()));
		response.addCookie(tokenProvider.createCookie("refresh_token", tokenDto.refreshToken()));
		tokenService.createRefreshToken(user, tokenDto.refreshToken(), tokenDto);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "로그인 성공"));
	}

	@DeleteMapping("/v1/auth/logout")
	public ResponseEntity<SuccessResponse> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
		HttpServletRequest request, HttpServletResponse response) {
		Long userId = userDetails.getUserId();

		String refreshToken = tokenProvider.getCookieValue(request, "refresh_token");
		tokenService.deleteRefreshToken(userId, refreshToken);

		tokenProvider.clearCookie(response, "access_token");
		tokenProvider.clearCookie(response, "refresh_token");

		SecurityContextHolder.clearContext();

		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "로그아웃 성공"));
	}

	@GetMapping("/v1/auth/check-nickname")
	public ResponseEntity<SuccessResponse> checkNickname(@RequestParam("nickname") @Nickname String nickname) {
		userService.checkNicknameDuplicate(nickname);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "사용 가능한 닉네임입니다"));
	}

	@GetMapping("/v1/auth/check-email")
	public ResponseEntity<SuccessResponse> checkEmail(@RequestParam("email") @Email String email) {
		userService.checkEmailDuplicate(email);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "사용 가능한 이메일입니다"));
	}

	@PostMapping("/v1/auth/confirm-password")
	public ResponseEntity<SuccessResponse> confirmPassword(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Password String password) {
		User user = userDetails.getUser();
		userService.validatePassword(user, password);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), "비밀번호 확인"));
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
