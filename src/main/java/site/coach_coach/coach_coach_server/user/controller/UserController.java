package site.coach_coach.coach_coach_server.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.jwt.TokenDto;
import site.coach_coach.coach_coach_server.user.dto.LoginRequest;
import site.coach_coach.coach_coach_server.user.dto.SignUpRequest;
import site.coach_coach.coach_coach_server.user.exception.IncorrectPasswordException;
import site.coach_coach.coach_coach_server.user.service.UserService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/v1/auth/signup")
	public ResponseEntity<Void> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
		userService.signup(signUpRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/v1/auth/login")
	public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginRequest loginRequest) throws
		IncorrectPasswordException {
		TokenDto tokenDto = userService.login(loginRequest);
		return ResponseEntity.noContent().build();
	}

}
