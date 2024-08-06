package site.coach_coach.coach_coach_server.routine.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequestDto;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;

@Service
@RequiredArgsConstructor
public class RoutineServices {
	private final RoutineRepository routineRepository;
	private final MatchingRepository matchingRepository;
	private final CoachRepository coachRepository;

	public Boolean findIsMatchingServices(RoutineListRequestDto routineListRequestDto) {
		return matchingRepository.findByUserIdAndCoachId(routineListRequestDto.userId(),
			routineListRequestDto.coachId()).getIsMatching();
	}

	public RoutineListCoachInfoDto findRoutineListCoachInfoServices(RoutineListRequestDto routineListRequestDto) {
		return coachRepository.findRoutineListCoachInfo(routineListRequestDto.coachId());
	}

	public List<RoutineForListDto> findRoutineForListServices(RoutineListRequestDto routineListRequestDto) {
		if (routineListRequestDto.coachId() != null) {
			return routineRepository.findRoutineListByCoach(routineListRequestDto.userId(),
				routineListRequestDto.coachId());
		} else {
			return routineRepository.findRoutineListByMyself(routineListRequestDto.userId());

		}
	}

}
