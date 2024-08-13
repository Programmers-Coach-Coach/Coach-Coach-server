package site.coach_coach.coach_coach_server.routine.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.services.RoutineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {
	private final RoutineService routineService;

	@GetMapping("/v1/routines")
	public ResponseEntity getRoutineList(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "coachId", required = false) Long coachIdParam,
		@RequestParam(name = "userId", required = false) Long userIdParam) {
		Long userIdByJWT = userDetails.getUserId();
		RoutineListRequest routineListRequest;

		if (coachIdParam == null && userIdParam == null) {
			routineListRequest = new RoutineListRequest(userIdByJWT, null);
			List<RoutineForListDto> routineListByMyself = routineService.getRoutineForList(routineListRequest);
			return ResponseEntity.ok(routineListByMyself);
		} else if (coachIdParam == null) {
			Long coachId = routineService.getCoachId(userIdByJWT);
			routineListRequest = new RoutineListRequest(userIdParam, coachId);
		} else {
			routineListRequest = new RoutineListRequest(userIdByJWT, coachIdParam);
		}

		routineService.getIsMatching(routineListRequest);

		List<RoutineForListDto> routineListByCoach = routineService.getRoutineForList(routineListRequest);
		return ResponseEntity.ok(routineListByCoach);
	}

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
