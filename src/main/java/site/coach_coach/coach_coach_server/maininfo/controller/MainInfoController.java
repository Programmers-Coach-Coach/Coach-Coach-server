package site.coach_coach.coach_coach_server.maininfo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.maininfo.dto.MainResponseDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainService;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainInfoController {
	private final MainService mainService;

	@GetMapping("/v1/main")
	public ResponseEntity<MainResponseDto> getMainInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null || userDetails.getUser() == null) {
			throw new InvalidUserException();
		}

		User user = userDetails.getUser();
		MainResponseDto mainResponse = mainService.getMainResponse(user);

		return ResponseEntity.ok(mainResponse);
	}
}
