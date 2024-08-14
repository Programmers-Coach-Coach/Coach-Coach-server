package site.coach_coach.coach_coach_server.routine.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.matching.domain.Matching;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.exception.NotMatchingException;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RoutineService {
	private final RoutineRepository routineRepository;
	private final MatchingRepository matchingRepository;
	private final CoachRepository coachRepository;
	private final UserRepository userRepository;

	public void checkIsMatching(RoutineListRequest routineListRequest) {
		if (routineListRequest.coachId() != null) {
			matchingRepository.findByUserIdAndCoachId(routineListRequest.userId(), routineListRequest.coachId())
				.map(Matching::getIsMatching)
				.filter(isMatching -> isMatching) // isMatching이 true일 때만 통과
				.orElseThrow(() -> new NotMatchingException(ErrorMessage.NOT_MATCHING));
		}
	}

	public Long getCoachId(Long userIdByJwt) {
		return userRepository.findById(userIdByJwt)
			.orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_FOUND_COACH))
			.getCoach().getCoachId();
	}

	public List<RoutineForListDto> getRoutineForList(RoutineListRequest routineListRequest) {
		List<RoutineForListDto> routineForListDtos = new ArrayList<>();
		List<Routine> routineList;

		if (routineListRequest.coachId() == null) {
			routineList = routineRepository.findByUserIdAndCoachIdIsNull(routineListRequest.userId());
		} else {
			routineList = routineRepository.findByUserIdAndCoachId(routineListRequest.userId(),
				routineListRequest.coachId());
		}

		routineList.forEach((routine) -> {
			RoutineForListDto dto = RoutineForListDto.builder()
				.routineId(routine.getRoutineId())
				.routineName(routine.getRoutineName())
				.sportName(routine.getSport().getSportName())
				.build();
			routineForListDtos.add(dto);
		});

		return routineForListDtos;
	}
}
