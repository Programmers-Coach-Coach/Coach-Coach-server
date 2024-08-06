package site.coach_coach.coach_coach_server.routine.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequestDto;
import site.coach_coach.coach_coach_server.routine.services.RoutineServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {
	private final RoutineServices routineServices;

	@GetMapping("/v1/routines")
	public ResponseEntity routines(@RequestParam("coachId") Long coachId) {
		Long userId = 2L;
		RoutineListRequestDto routineListRequestDto = new RoutineListRequestDto(userId, coachId);

		if (coachId == null) {
			List<RoutineForListDto> routineListByMyself = routineServices.findRoutineForListServices(
				routineListRequestDto);

			return ResponseEntity.status(HttpStatus.OK).body(routineListByMyself);
		} else {
			Boolean checkMatching = routineServices.findIsMatchingServices(routineListRequestDto);
			if (!checkMatching) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("매칭되지 않은 코치입니다.");
			} else {
				RoutineListCoachInfoDto routineListCoachInfoDto = routineServices.findRoutineListCoachInfoServices(
					routineListRequestDto);
				List<RoutineForListDto> routineListByCoach = routineServices.findRoutineForListServices(
					routineListRequestDto);

				RoutineListDto routineListDto = new RoutineListDto(routineListCoachInfoDto, routineListByCoach);
				return ResponseEntity.status(HttpStatus.OK).body(routineListDto);
			}
		}
	}

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
