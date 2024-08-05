package site.coach_coach.coach_coach_server.routine.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.common.exception.GlobalExceptionHandler;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListDto;
import site.coach_coach.coach_coach_server.routine.services.RoutineServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoutineController {
	private final RoutineServices routineServices;
	private final GlobalExceptionHandler globalExceptionHandler;

	private static final Logger logger = LoggerFactory.getLogger(RoutineController.class);

	@GetMapping("/v1/routines")
	public ResponseEntity routines(@RequestParam("coachId") Optional<Long> coachId) {
		Long userId = 2L;
		Optional<Integer> isMatching = routineServices.searchIsMatching(userId, coachId);
		if (isMatching.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 coachId를 입력하셨습니다.");
		}

		List<RoutineForListDto> routineForListDto = routineServices.searchRoutines(userId, coachId);

		Optional<RoutineListCoachInfoDto> routineListCoachInfoDto = null;
		if (!coachId.isEmpty()) {
			routineListCoachInfoDto = routineServices.searchRoutineCoachInfo(coachId);
		}
		RoutineListDto routineListDto = new RoutineListDto(routineListCoachInfoDto, routineForListDto);
		return ResponseEntity.status(HttpStatus.OK).body(routineListDto);

	}

	@GetMapping("/v1/test")
	public ResponseEntity test() {
		return ResponseEntity.status(HttpStatus.OK).body("테스트 성공!");
	}
}
