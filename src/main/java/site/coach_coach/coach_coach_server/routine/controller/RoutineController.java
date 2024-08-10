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
import site.coach_coach.coach_coach_server.routine.services.RoutineServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {
	private final RoutineServices routineServices;

	@GetMapping("/v1/routines")
	public ResponseEntity routines(@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "coachId", required = false) Long coachId) {

		Long userId = userDetails.getUserId();

		RoutineListRequest routineListRequest = new RoutineListRequest(userId, coachId);

		if (coachId == null) {
			List<RoutineForListDto> routineListByMyself = routineServices.findRoutineForListServices(
				routineListRequest);

			return ResponseEntity.status(HttpStatus.OK).body(routineListByMyself);
		} else {
			Boolean checkMatching = routineServices.findIsMatchingServices(routineListRequest);
			if (!checkMatching) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아직 매칭이 되지 않은 코치입니다.");
			} else {
				RoutineListCoachInfoDto routineListCoachInfoDto = routineServices.findRoutineListCoachInfoServices(
					routineListRequest);
				List<RoutineForListDto> routineListByCoach = routineServices.findRoutineForListServices(
					routineListRequest);

				RoutineListResponse routineListResponse = new RoutineListResponse(routineListCoachInfoDto,
					routineListByCoach);
				return ResponseEntity.status(HttpStatus.OK).body(routineListResponse);
			}
		}
	}

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
