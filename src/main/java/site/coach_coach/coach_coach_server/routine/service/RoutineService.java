package site.coach_coach.coach_coach_server.routine.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.matching.domain.Matching;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.dto.CreateRoutineRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.UserInfoForRoutineList;
import site.coach_coach.coach_coach_server.routine.exception.NotMatchingException;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RoutineService {
	private static final Logger log = LoggerFactory.getLogger(RoutineService.class);
	private final RoutineRepository routineRepository;
	private final MatchingRepository matchingRepository;
	private final CoachRepository coachRepository;
	private final UserRepository userRepository;
	private final SportRepository sportRepository;

	public void checkIsMatching(RoutineListRequest routineListRequest) {
		matchingRepository.findByUserIdAndCoachId(routineListRequest.userId(), routineListRequest.coachId())
			.map(Matching::getIsMatching)
			.filter(isMatching -> isMatching) // isMatching이 true일 때만 통과
			.orElseThrow(() -> new NotMatchingException(ErrorMessage.NOT_MATCHING));
	}

	public RoutineListRequest createRoutineListRequest(Long userIdParam, Long coachIdParam, Long userIdByJwt) {
		if (coachIdParam == null) {
			Long coachId = getCoachId(userIdByJwt);
			return new RoutineListRequest(userIdParam, coachId);
		} else {
			return new RoutineListRequest(userIdByJwt, coachIdParam);
		}
	}

	public RoutineListRequest confirmIsMatching(Long userIdParam, Long coachIdParam, Long userIdByJwt) {
		if (coachIdParam == null && userIdParam == null) {
			return new RoutineListRequest(userIdByJwt, null);
		} else {
			RoutineListRequest request = createRoutineListRequest(userIdParam, coachIdParam, userIdByJwt);
			checkIsMatching(request);
			return request;
		}
	}

	public Long getCoachId(Long userIdByJwt) {
		return coachRepository.findCoachIdByUserId(userIdByJwt)
			.orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_FOUND_COACH));
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

	public UserInfoForRoutineList getUserInfoForRoutineList(Long userIdParam, Long coachIdParam) {
		if (userIdParam == null) {
			User userInfo = coachRepository.findById(coachIdParam)
				.orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_FOUND_COACH))
				.getUser();
			return new UserInfoForRoutineList(userInfo.getUserId(), userInfo.getNickname(),
				userInfo.getProfileImageUrl());
		} else {
			User userInfo = userRepository.findById(userIdParam)
				.orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_FOUND_USER));
			return new UserInfoForRoutineList(userIdParam, userInfo.getNickname(),
				userInfo.getProfileImageUrl());
		}
	}

	public void createRoutine(CreateRoutineRequest createRoutineRequest, Long userIdByJwt) {
		Sport sportInfo = Sport.builder()
			.sportId(createRoutineRequest.sportId())
			.build();

		if (createRoutineRequest.userId() == null) {
			Routine newRoutine = Routine.builder()
				.userId(userIdByJwt)
				.routineName(createRoutineRequest.routineName())
				.sport(sportInfo)
				.build();
			routineRepository.save(newRoutine);
		} else {
			Routine newRoutine = Routine.builder()
				.userId(createRoutineRequest.userId())
				.coachId(userIdByJwt)
				.routineName(createRoutineRequest.routineName())
				.sport(sportInfo)
				.build();
			routineRepository.save(newRoutine);
		}
	}
}
