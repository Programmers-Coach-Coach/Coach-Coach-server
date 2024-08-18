package site.coach_coach.coach_coach_server.routine.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.routine.dto.CreateRoutineRequest;
import site.coach_coach.coach_coach_server.routine.dto.CreateRoutineResponse;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.UserInfoForRoutineList;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

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

		List<RoutineForListDto> routineListResponse = routineService.getRoutineForList(routineListRequest);
		return ResponseEntity.ok(routineListResponse);
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

	@PostMapping("/v1/routines")
	public ResponseEntity<CreateRoutineResponse> createRoutine(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid CreateRoutineRequest createRoutineRequest
	) {
		Long userIdByJwt = userDetails.getUserId();
		Long newRoutineId = routineService.createRoutine(createRoutineRequest, userIdByJwt);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new CreateRoutineResponse(HttpStatus.CREATED.value(), newRoutineId));
	}

	// @GetMapping("/v1/routines/{routineId}")
	// public ResponseEntity<RoutineResponse> getRoutine(
	// 	@AuthenticationPrincipal CustomUserDetails userDetails,
	// 	@PathVariable(name = "routineId") @Valid Long routineId
	// ) {
	// 	Long userIdByJwt = userDetails.getUserId();
	// 	// 조회하려는 루틴에 접근 가능한지 확인
	// 	// 개별 루틴 - 카테고리 - 운동 정보 조회하여 반환
	// }

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
