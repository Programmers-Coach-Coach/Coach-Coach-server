package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;

public record RoutineListDto(
	float completionPercentage,

	List<RoutineDto> routines
) {
	public RoutineListDto setCompletionPercentage(float completionPercentage) {
		return new RoutineListDto(completionPercentage, this.routines);
	}
}
