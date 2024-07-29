package site.coach_coach.coach_coach_server.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.user.dto.SignupDto;
import site.coach_coach.coach_coach_server.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping("/auth/signup")
	public ResponseEntity<Void> signup(@RequestBody @Valid SignupDto signupDto) {
		userService.signup(signupDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
