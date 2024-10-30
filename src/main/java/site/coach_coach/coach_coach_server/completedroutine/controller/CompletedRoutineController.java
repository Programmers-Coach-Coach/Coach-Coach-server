package site.coach_coach.coach_coach_server.completedroutine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.response.SuccessIdResponse;
import site.coach_coach.coach_coach_server.completedroutine.service.CompletedRoutineService;
import site.coach_coach.coach_coach_server.routine.service.RoutineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompletedRoutineController {
	private final CompletedRoutineService completedRoutineService;
	private final RoutineService routineService;

	@PostMapping("/v2/routines/{routineId}/completed")
	public ResponseEntity<SuccessIdResponse> createRoutineCompletion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "routineId") Long routineId
	) {
		Long userIdByJwt = userDetails.getUserId();
		Long completedRoutineId = completedRoutineService.createCompletedRoutine(routineId, userIdByJwt);
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessIdResponse(completedRoutineId));

	}

	@DeleteMapping("/v2/routines/{routineId}/completed")
	public ResponseEntity<Void> deleteRoutineCompletion(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "routineId") Long routineId
	) {
		Long userIdByJwt = userDetails.getUserId();
		completedRoutineService.deleteCompletedRoutine(routineId, userIdByJwt);
		return ResponseEntity.noContent().build();
	}

}
