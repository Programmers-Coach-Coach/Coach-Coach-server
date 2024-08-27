package site.coach_coach.coach_coach_server.maininfo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.maininfo.dto.MainInfoResponseDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainInfoService;
import site.coach_coach.coach_coach_server.user.domain.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainInfoController {
	private final MainInfoService mainInfoService;

	@GetMapping("/v1/main-info")
	public ResponseEntity<MainInfoResponseDto> getMainInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		User user = userDetails.getUser();
		MainInfoResponseDto mainInfoResponse = mainInfoService.getMainInfoResponse(user);

		return ResponseEntity.ok(mainInfoResponse);
	}
}
