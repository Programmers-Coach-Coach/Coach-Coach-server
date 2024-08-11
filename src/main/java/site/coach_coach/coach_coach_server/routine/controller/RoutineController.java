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
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListResponse;
import site.coach_coach.coach_coach_server.routine.services.RoutineService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {
	private final RoutineService routineService;

	@GetMapping("/v1/routines")
	public ResponseEntity getRoutineList(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "coachId", required = false) Long coachId) {
		Long userId = userDetails.getUserId();
		RoutineListRequest routineListRequest = new RoutineListRequest(userId, coachId);

		if (coachId == null) {
			return getRoutineListByMyself(routineListRequest);
		} else {
			Boolean isMatching = routineService.getIsMatching(routineListRequest);
			if (!isMatching) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("message : 매칭되지 않은 코치입니다.");
			} else {
				return getRoutineListByCoach(routineListRequest);
			}
		}
	}

	private ResponseEntity<RoutineListResponse> getRoutineListByMyself(RoutineListRequest routineListRequest) {
		List<RoutineForListDto> routineListByMyself = routineService.getRoutineForList(routineListRequest);
		RoutineListResponse routineListResponse = new RoutineListResponse(null, routineListByMyself);
		return ResponseEntity.ok(routineListResponse);
	}

	private ResponseEntity<RoutineListResponse> getRoutineListByCoach(RoutineListRequest routineListRequest) {
		RoutineListCoachInfoDto routineListCoachInfoDto = routineService.getRoutineListCoachInfo(
			routineListRequest);
		List<RoutineForListDto> routineListByCoach = routineService.getRoutineForList(routineListRequest);
		RoutineListResponse routineListResponse = new RoutineListResponse(routineListCoachInfoDto,
			routineListByCoach);
		return ResponseEntity.ok(routineListResponse);
	}

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
