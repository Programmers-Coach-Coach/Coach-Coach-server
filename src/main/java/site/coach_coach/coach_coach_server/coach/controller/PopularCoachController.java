package site.coach_coach.coach_coach_server.coach.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.coach.dto.PopularCoachDto;
import site.coach_coach.coach_coach_server.coach.service.PopularCoachService;
import site.coach_coach.coach_coach_server.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PopularCoachController {

	private final PopularCoachService popularCoachService;

	@GetMapping("/popular-coaches")
	public ResponseEntity<List<PopularCoachDto>> getPopularCoaches(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();
		List<PopularCoachDto> coaches = popularCoachService.getTopCoaches(user);
		return ResponseEntity.ok(coaches);
	}
}
