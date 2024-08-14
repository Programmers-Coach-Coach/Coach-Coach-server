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
import site.coach_coach.coach_coach_server.routine.dto.UserInfoForRoutineList;
import site.coach_coach.coach_coach_server.routine.services.RoutineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {
	private final RoutineService routineService;

	@GetMapping("/v1/routines")
	public ResponseEntity<List<RoutineForListDto>> getRoutineList(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "userId", required = false) Long userIdParam,
		@RequestParam(name = "coachId", required = false) Long coachIdParam) {

		Long userIdByJwt = userDetails.getUserId();
		RoutineListRequest routineListRequest = routineService.confirmIsMatching(userIdParam, coachIdParam,
			userIdByJwt);

		List<RoutineForListDto> routineList = routineService.getRoutineForList(routineListRequest);
		return ResponseEntity.ok(routineList);
	}

	@GetMapping("/v1/routines/user")
	public ResponseEntity<UserInfoForRoutineList> getUserInfoForRoutineList(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "userId", required = false) Long userIdParam,
		@RequestParam(name = "coachId", required = false) Long coachIdParam) {

		Long userIdByJwt = userDetails.getUserId();
		routineService.confirmIsMatching(userIdParam, coachIdParam, userIdByJwt);

		UserInfoForRoutineList userInfoForRoutineList = routineService.getUserInfoForRoutineList(userIdParam,
			coachIdParam);

		return ResponseEntity.ok(userInfoForRoutineList);
	}

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
