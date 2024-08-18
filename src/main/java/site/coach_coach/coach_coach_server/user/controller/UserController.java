package site.coach_coach.coach_coach_server.user.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenProvider;
import site.coach_coach.coach_coach_server.auth.jwt.dto.TokenDto;
import site.coach_coach.coach_coach_server.auth.jwt.service.TokenService;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.common.utils.AmazonS3Uploader;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.PasswordRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.dto.UserProfileRequest;
import site.coach_coach.coach_coach_server.user.dto.UserProfileResponse;
import site.coach_coach.coach_coach_server.user.service.UserService;
import site.coach_coach.coach_coach_server.user.validation.Nickname;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final TokenService tokenService;
	private final TokenProvider tokenProvider;
	private final AmazonS3Uploader amazonS3Uploader;

	@PostMapping("/v1/auth/signup")
	public ResponseEntity<SuccessResponse> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
		userService.signup(signUpRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new SuccessResponse(HttpStatus.CREATED.value(), SuccessMessage.SIGNUP_SUCCESS.getMessage()));
	}

	@PostMapping("/v1/auth/login")
	public ResponseEntity<SuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest,
		HttpServletResponse response) {
		User user = userService.validateUser(loginRequest);
		TokenDto tokenDto = userService.createJwt(user);

		//TODO : 서비스 배포 후 localhost 쿠키 삭제
		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("access_token", tokenDto.accessToken(), "localhost").toString());
		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("refresh_token", tokenDto.refreshToken(), "localhost").toString());

		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("access_token", tokenDto.accessToken(), ".coach-coach.site").toString());
		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("refresh_token", tokenDto.refreshToken(), ".coach-coach.site").toString());

		tokenService.createRefreshToken(user, tokenDto.refreshToken(), tokenDto);
		return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.LOGIN_SUCCESS.getMessage()));
	}

	@DeleteMapping("/v1/auth/logout")
	public ResponseEntity<SuccessResponse> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
		HttpServletRequest request, HttpServletResponse response) {
		Long userId = userDetails.getUserId();

		String refreshToken = tokenProvider.getCookieValue(request, "refresh_token");
		tokenService.deleteRefreshToken(userId, refreshToken);

		//TODO : 서비스 배포 후 localhost 쿠키 삭제
		response.addHeader("Set-Cookie", tokenProvider.clearCookie("access_token", "localhost").toString());
		response.addHeader("Set-Cookie", tokenProvider.clearCookie("refresh_token", "localhost").toString());
		response.addHeader("Set-Cookie", tokenProvider.clearCookie("access_token", ".coach-coach.site").toString());
		response.addHeader("Set-Cookie", tokenProvider.clearCookie("refresh_token", ".coach-coach.site").toString());

		SecurityContextHolder.clearContext();

		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.LOGOUT_SUCCESS.getMessage())
		);
	}

	@GetMapping("/v1/auth/check-nickname")
	public ResponseEntity<SuccessResponse> checkNickname(@RequestParam("nickname") @Nickname String nickname) {
		userService.checkNicknameDuplicate(nickname);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.NICKNAME_AVAILABLE.getMessage())
		);
	}

	@GetMapping("/v1/auth/check-email")
	public ResponseEntity<SuccessResponse> checkEmail(@RequestParam("email") @Email String email) {
		userService.checkEmailDuplicate(email);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.NICKNAME_AVAILABLE.getMessage())
		);
	}

	@PostMapping("/v1/auth/confirm-password")
	public ResponseEntity<SuccessResponse> confirmPassword(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid PasswordRequest passwordRequest) {
		Long userId = userDetails.getUserId();
		userService.validatePassword(userId, passwordRequest);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.PASSWORD_CONFIRM_SUCCESS.getMessage())
		);
	}

	@GetMapping("/v1/user/me")
	public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUserId();
		return ResponseEntity.ok(userService.getUserProfile(userId));
	}

	@PutMapping("/v1/user/me")
	public ResponseEntity<SuccessResponse> updateMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
		@RequestPart("userProfileRequest") String userProfileRequestJson
	) throws IOException {
		Long userId = userDetails.getUserId();

		ObjectMapper objectMapper = new ObjectMapper();
		UserProfileRequest userProfileRequest = objectMapper.readValue(userProfileRequestJson,
			UserProfileRequest.class);

		userService.updateUserProfile(userId, profileImage, userProfileRequest);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.UPDATE_PROFILE_SUCCESS.getMessage())
		);
	}

	@GetMapping("/v1/auth/reissue")
	public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = tokenProvider.getCookieValue(request, "refresh_token");

		String newAccessToken = tokenService.reissueAccessToken(refreshToken);
		response.addHeader("Set-Cookie", tokenProvider.clearCookie("access_token", "localhost").toString());
		response.addHeader("Set-Cookie", tokenProvider.clearCookie("access_token", ".coach-coach.site").toString());

		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("access_token", newAccessToken, "localhost").toString());
		response.addHeader("Set-Cookie",
			tokenProvider.createCookie("access_token", newAccessToken, ".coach-coach.site").toString());

		return ResponseEntity.noContent().build();
	}
}
