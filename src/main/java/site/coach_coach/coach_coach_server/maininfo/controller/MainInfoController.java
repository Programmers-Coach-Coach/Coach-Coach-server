package site.coach_coach.coach_coach_server.maininfo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.maininfo.dto.MainInfoCoachDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainInfoService;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainInfoController {
	private final MainInfoService mainInfoService;

	@GetMapping("/v1/popular-coaches")
	public ResponseEntity<List<MainInfoCoachDto>> getPopularCoaches(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<MainInfoCoachDto> coaches = mainInfoService.getTopCoaches(userDetails.getUser());
		return ResponseEntity.ok(coaches);
	}

	@GetMapping("/v1/sports")
	public ResponseEntity<List<SportDto>> getSports() {
		List<SportDto> sports = mainInfoService.getSports();
		return ResponseEntity.ok(sports);
	}
}
