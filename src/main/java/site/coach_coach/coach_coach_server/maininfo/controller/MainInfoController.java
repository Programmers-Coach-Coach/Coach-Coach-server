package site.coach_coach.coach_coach_server.maininfo.controller;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.maininfo.dto.MainResponseDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainService;
import site.coach_coach.coach_coach_server.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainInfoController {
	private final MainService mainService;

	@GetMapping("/v1/main")
	public ResponseEntity<MainResponseDto> getMainInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();
		MainResponseDto mainResponse = mainService.getMainResponse(user);

		return ResponseEntity.ok(mainResponse);
	}
}
