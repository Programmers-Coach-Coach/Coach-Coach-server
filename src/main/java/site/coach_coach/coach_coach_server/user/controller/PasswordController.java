package site.coach_coach.coach_coach_server.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.user.dto.ResetPasswordRequest;
import site.coach_coach.coach_coach_server.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PasswordController {

	private final UserService userService;

	@PatchMapping("/reset-password")
	public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
		userService.resetPassword(request.getEmail(), request.getPassword());
		return ResponseEntity.noContent().build();
	}
}
