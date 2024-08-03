package site.coach_coach.coach_coach_server.routine.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;

@Service
@RequiredArgsConstructor
public class RoutineServices {
	private final RoutineRepository routineRepository;

	public List<RoutineForListDto> searchRoutines(Long userId, Optional<Long> coachId) {
		if (coachId.isEmpty()) {
			return routineRepository.findMyRoutines(userId);
		} else {
			return routineRepository.findRoutinesByCoach(userId, coachId);
		}
	}

	public Optional<RoutineListCoachInfoDto> searchRoutineCoachInfo(Optional<Long> coachId) {
		return routineRepository.findRoutineListCoachInfo(coachId);
	}
}
