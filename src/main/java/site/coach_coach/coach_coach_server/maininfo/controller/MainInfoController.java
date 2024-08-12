package site.coach_coach.coach_coach_server.maininfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.maininfo.dto.MainResponseDto;
import site.coach_coach.coach_coach_server.maininfo.service.MainService;
import site.coach_coach.coach_coach_server.user.domain.User;


@RestController
@RequestMapping("/api/v1/main")
public class MainInfoController {

	private final MainService mainService;

	@Autowired
	public MainInfoController(MainService mainService) {
		this.mainService = mainService;
	}

	@GetMapping
	public MainResponseDto getMain(@AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userDetails.getUser();
		return mainService.getMainResponse(user);
	}
}
