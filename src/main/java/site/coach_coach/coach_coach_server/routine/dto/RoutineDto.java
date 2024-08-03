package site.coach_coach.coach_coach_server.routine.dto;

import lombok.Builder;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

@Builder
public record RoutineDto(
	Long userId,
	Long coachId,
	Long sportsId,
	String routineName
) {
	public static RoutineDto from(Routine routine) {
		return RoutineDto.builder()
			.userId(routine.getUserId().longValue())
			.coachId(routine.getCoachId().longValue())
			.sportsId(routine.getSportId().longValue())
			.routineName(routine.getRoutineName())
			.build();
	}
}
