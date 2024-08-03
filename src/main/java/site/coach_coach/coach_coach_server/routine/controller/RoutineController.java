package site.coach_coach.coach_coach_server.routine.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListDto;
import site.coach_coach.coach_coach_server.routine.services.RoutineServices;

@RestController
@RequiredArgsConstructor
public class RoutineController {
	private final RoutineServices routineServices;

	private static final Logger logger = LoggerFactory.getLogger(RoutineController.class);

	@GetMapping("/routines")
	public ResponseEntity routines(@RequestParam("coachId") Optional<Long> coachId) {
		Long userId = 2L;
		try {
			List<RoutineForListDto> routineForListDto = routineServices.searchRoutines(userId, coachId);

			Optional<RoutineListCoachInfoDto> routineListCoachInfoDto = null;
			if (!coachId.isEmpty()) {
				routineListCoachInfoDto = routineServices.searchRoutineCoachInfo(coachId);
			}
			RoutineListDto routineListDto = new RoutineListDto(routineListCoachInfoDto, routineForListDto);
			return ResponseEntity.status(HttpStatus.OK).body(routineListDto);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("coachId 입력 오류!");
		}

	}

	@GetMapping("/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
