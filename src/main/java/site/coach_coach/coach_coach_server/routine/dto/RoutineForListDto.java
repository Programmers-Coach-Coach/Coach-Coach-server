package site.coach_coach.coach_coach_server.routine.dto;

import lombok.Builder;

@Builder
public record RoutineForListDto(
	Long routineId,

	String routineName,

	String sportName
) {

}
