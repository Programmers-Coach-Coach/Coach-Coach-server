package site.coach_coach.coach_coach_server.userrecord.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.completedroutine.dto.CompletedRoutineDto;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

public record RecordsDto(
	String routineName,
	List<CompletedRoutineDto> completedRoutines
) {
	public static RecordsDto from(Routine routine, List<CompletedRoutineDto> completedRoutines) {
		return new RecordsDto(
			routine != null ? routine.getRoutineName() : null,
			completedRoutines
		);
	}
}
