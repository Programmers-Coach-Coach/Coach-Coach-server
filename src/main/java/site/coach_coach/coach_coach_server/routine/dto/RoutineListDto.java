package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class RoutineListDto {
	private RoutineListCoachInfoDto routineListCoachInfoDto;
	private List<RoutineForListDto> routineForListDtos;

}
