package site.coach_coach.coach_coach_server.routine.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.matching.domain.Matching;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.dto.CreateRoutineRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.dto.RoutineResponse;
import site.coach_coach.coach_coach_server.routine.dto.UserInfoForRoutineList;
import site.coach_coach.coach_coach_server.routine.exception.NoExistRoutineException;
import site.coach_coach.coach_coach_server.routine.exception.NotMatchingException;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;
import site.coach_coach.coach_coach_server.sport.domain.Sport;
import site.coach_coach.coach_coach_server.sport.repository.SportRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoutineService {
	private final RoutineRepository routineRepository;
	private final MatchingRepository matchingRepository;
	private final CoachRepository coachRepository;
	private final UserRepository userRepository;
	private final SportRepository sportRepository;

	@Transactional(readOnly = true)
	public void checkIsMatching(Long userId, Long coachId) {
		matchingRepository.findByUser_UserIdAndCoach_CoachId(userId, coachId)
			.map(Matching::getIsMatching)
			.filter(isMatching -> isMatching) // isMatching이 true일 때만 통과
			.orElseThrow(() -> new NotMatchingException(ErrorMessage.NOT_MATCHING));
	}

	private RoutineListRequest createRoutineListRequest(Long userIdParam, Long coachIdParam, Long userIdByJwt) {
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
			checkIsMatching(request.userId(), request.coachId());
			return request;
		}
	}

	@Transactional(readOnly = true)
	public Long getCoachId(Long userIdByJwt) {
		return coachRepository.findCoachIdByUserId(userIdByJwt)
			.orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_FOUND_COACH));
	}

	@Transactional(readOnly = true)
	public List<RoutineForListDto> getRoutineForList(RoutineListRequest routineListRequest) {
		List<RoutineForListDto> routineListResponse = new ArrayList<>();
		List<Routine> routines;

		if (routineListRequest.coachId() == null) {
			routines = routineRepository.findByUserIdAndCoachIdIsNull(routineListRequest.userId());
		} else {
			routines = routineRepository.findByUserIdAndCoachId(routineListRequest.userId(),
				routineListRequest.coachId());
		}

		routines.forEach((routine) -> {
			RoutineForListDto dto = RoutineForListDto.builder()
				.routineId(routine.getRoutineId())
				.routineName(routine.getRoutineName())
				.sportName(routine.getSport().getSportName())
				.build();
			routineListResponse.add(dto);
		});

		return routineListResponse;
	}

	@Transactional(readOnly = true)
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

	@Transactional
	public Long createRoutine(CreateRoutineRequest createRoutineRequest, Long userIdByJwt) {
		Sport sportInfo = Sport.builder()
			.sportId(createRoutineRequest.sportId())
			.build();

		Routine.RoutineBuilder routineBuilder = Routine.builder()
			.userId(createRoutineRequest.userId() == null ? userIdByJwt : createRoutineRequest.userId())
			.routineName(createRoutineRequest.routineName())
			.sport(sportInfo);

		if (createRoutineRequest.userId() != null) {
			Long coachId = getCoachId(userIdByJwt);
			checkIsMatching(createRoutineRequest.userId(), coachId);
			routineBuilder.coachId(coachId);
		}

		return routineRepository.save(routineBuilder.build()).getRoutineId();
	}

	@Transactional(readOnly = true)
	public RoutineResponse getRoutineDetail(Long routineId, Long userIdByJwt, Long userIdParam) {
		Routine routine = routineRepository.findById(routineId)
			.orElseThrow(() -> new NoExistRoutineException(ErrorMessage.NOT_FOUND_ROUTINE));

		validateBeforeGetRoutine(routine, userIdParam, userIdByJwt);
		return RoutineResponse.from(routine);
	}

	private void validateBeforeGetRoutine(Routine routine, Long userIdParam, Long userIdByJwt) {
		if (userIdParam == null) {
			if (!routine.getUserId().equals(userIdByJwt)) {
				throw new AccessDeniedException();
			}
		} else {
			Long coachId = getCoachId(userIdByJwt);
			if (routine.getCoachId() != coachId || !routine.getUserId().equals(userIdParam)) {
				throw new AccessDeniedException();
			}
		}
	}

	@Transactional
	public void deleteRoutine(Long routineId, Long userIdByJwt) {
		validateIsMyRoutine(routineId, userIdByJwt);
		routineRepository.deleteById(routineId);
	}

	@Transactional
	public Routine validateIsMyRoutine(Long routineId, Long userIdByJwt) {
		Routine routine = routineRepository.findById(routineId)
			.orElseThrow(() -> new NoExistRoutineException(ErrorMessage.NOT_FOUND_ROUTINE));

		if (routine.getCoachId() == null) {
			if (!routine.getUserId().equals(userIdByJwt)) {
				throw new AccessDeniedException();
			}
		} else {
			Long coachId = getCoachId(userIdByJwt);
			if (!routine.getCoachId().equals(coachId)) {
				throw new AccessDeniedException();
			}
		}
		return routine;
	}

	@Transactional(readOnly = true)
	public void validateBeforeCompleteCategory(Long routineId, Long userIdByJwt) {
		Routine routine = routineRepository.findById(routineId)
			.orElseThrow(() -> new NoExistRoutineException(ErrorMessage.NOT_FOUND_ROUTINE));

		if (!routine.getUserId().equals(userIdByJwt)) {
			throw new AccessDeniedException();
		}
	}
}
