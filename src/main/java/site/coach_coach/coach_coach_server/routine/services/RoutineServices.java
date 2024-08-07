package site.coach_coach.coach_coach_server.routine.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.matching.repository.MatchingRepository;
import site.coach_coach.coach_coach_server.routine.domain.Routine;
import site.coach_coach.coach_coach_server.routine.dto.RoutineForListDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListCoachInfoDto;
import site.coach_coach.coach_coach_server.routine.dto.RoutineListRequest;
import site.coach_coach.coach_coach_server.routine.repository.RoutineRepository;
import site.coach_coach.coach_coach_server.user.domain.User;

@Service
@RequiredArgsConstructor
public class RoutineServices {
	private final RoutineRepository routineRepository;
	private final MatchingRepository matchingRepository;
	private final CoachRepository coachRepository;

	public Boolean findIsMatchingServices(RoutineListRequest routineListRequest) {
		return matchingRepository.findByUserIdAndCoachId(routineListRequest.userId(),
			routineListRequest.coachId()).getIsMatching();
	}

	public RoutineListCoachInfoDto findRoutineListCoachInfoServices(RoutineListRequest routineListRequest) {
		User coachInfoForRoutineList = coachRepository.findById(routineListRequest.coachId()).get().getUser();
		RoutineListCoachInfoDto routineListCoachInfoDto = RoutineListCoachInfoDto.builder()
			.coachId(routineListRequest.coachId())
			.nickname(coachInfoForRoutineList.getNickname())
			.profileImageUrl(coachInfoForRoutineList.getProfileImageUrl())
			.build();
		return routineListCoachInfoDto;
	}

	public List<RoutineForListDto> findRoutineForListServices(RoutineListRequest routineListRequest) {
		List<RoutineForListDto> routineForListDtos = new ArrayList<>();
		List<Routine> routineList;

		if (routineListRequest == null) {
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
			System.out.println(routine.getRoutineId());
			routineForListDtos.add(dto);
		});

		return routineForListDtos;
	}
}
