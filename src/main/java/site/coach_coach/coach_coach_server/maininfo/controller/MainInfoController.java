package site.coach_coach.coach_coach_server.maininfo.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import site.coach_coach.coach_coach_server.maininfo.dto.*;
import site.coach_coach.coach_coach_server.maininfo.service.*;

@RestController
@RequestMapping("/api/v1/main")
public class MainInfoController {

	private final MainService mainService;

	@Autowired
	public MainInfoController(MainService mainService) {
		this.mainService = mainService;
	}

	@GetMapping
	public MainResponseDto getMain(@RequestParam(required = false) Long userId) {
		return mainService.getMainResponse(userId);
	}
}
