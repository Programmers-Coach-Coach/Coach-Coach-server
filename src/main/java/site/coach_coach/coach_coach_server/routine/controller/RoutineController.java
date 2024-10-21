package site.coach_coach.coach_coach_server.routine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.action.service.ActionService;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.routine.dto.CreateRoutineRequest;
import site.coach_coach.coach_coach_server.routine.dto.CreateRoutineResponse;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListDto;
import site.coach_coach.coach_coach_server.routine.dto.UpdateRoutineInfoRequest;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {
	private final RoutineService routineService;
	private final ActionService actionService;

	@GetMapping("/v2/routines")
	public ResponseEntity<RoutineListDto> getRoutineList(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "userId", required = false) Long userIdParam,
		@RequestParam(name = "coachId", required = false) Long coachIdParam) {

		Long userIdByJwt = userDetails.getUserId();

		RoutineListDto routineListResponse = routineService.getRoutineList(userIdParam, coachIdParam,
			userIdByJwt);
		return ResponseEntity.ok(routineListResponse);
	}

	// @GetMapping("/v1/routines/user")
	// public ResponseEntity<UserInfoForRoutineList> getUserInfoForRoutineList(
	// 	@AuthenticationPrincipal CustomUserDetails userDetails,
	// 	@RequestParam(name = "userId", required = false) Long userIdParam,
	// 	@RequestParam(name = "coachId", required = false) Long coachIdParam) {
	//
	// 	Long userIdByJwt = userDetails.getUserId();
	//
	// 	routineService.confirmIsMatching(userIdParam, coachIdParam, userIdByJwt);
	// 	UserInfoForRoutineList userInfoForRoutineList = routineService.getUserInfoForRoutineList(userIdParam,
	// 		coachIdParam);
	//
	// 	return ResponseEntity.ok(userInfoForRoutineList);
	// }

	@PostMapping("/v2/routines")
	public ResponseEntity<CreateRoutineResponse> createRoutine(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid CreateRoutineRequest createRoutineRequest
	) {
		Long userIdByJwt = userDetails.getUserId();
		Long newRoutineId = routineService.createRoutine(createRoutineRequest, userIdByJwt);
		actionService.createAction(newRoutineId, userIdByJwt, createRoutineRequest.actions());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new CreateRoutineResponse(HttpStatus.CREATED.value(), newRoutineId));
	}

	@DeleteMapping("/v1/routines/{routineId}")
	public ResponseEntity<Void> deleteRoutine(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "routineId") Long routineId
	) {
		Long userIdByJwt = userDetails.getUserId();
		routineService.deleteRoutine(routineId, userIdByJwt);
		return ResponseEntity.noContent().build();
	}

	// @GetMapping("/v1/routines/{routineId}")
	// public ResponseEntity<RoutineResponse> getRoutine(
	// 	@AuthenticationPrincipal CustomUserDetails userDetails,
	// 	@PathVariable(name = "routineId") Long routineId,
	// 	@RequestParam(name = "userId", required = false) Long userIdParam
	// ) {
	// 	Long userIdByJwt = userDetails.getUserId();
	//
	// 	RoutineResponse routineResponse = routineService.getRoutineDetail(routineId,
	// 		userIdByJwt, userIdParam);
	// 	return ResponseEntity.ok(routineResponse);
	// }

	@PatchMapping("v1/routines/{routineId}")
	public ResponseEntity<Void> updateRoutineInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "routineId") Long routineId,
		@RequestBody @Valid UpdateRoutineInfoRequest updateRoutineInfoRequest
	) {
		Long userIdByJwt = userDetails.getUserId();
		routineService.updateRoutine(updateRoutineInfoRequest, routineId, userIdByJwt);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
