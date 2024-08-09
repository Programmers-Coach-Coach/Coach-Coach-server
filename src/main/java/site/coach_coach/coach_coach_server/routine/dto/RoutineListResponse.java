package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;

public record RoutineListResponse(
	RoutineListCoachInfoDto routineListCoachInfoDto,
	List<RoutineForListDto> routineForListDtos
) {

}
