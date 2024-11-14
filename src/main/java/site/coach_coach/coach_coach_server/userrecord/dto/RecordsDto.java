package site.coach_coach.coach_coach_server.userrecord.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.completedroutine.dto.CompletedRoutineDto;

public record RecordsDto(
	List<CompletedRoutineDto> completedRoutines
) {
	public static RecordsDto from(List<CompletedRoutineDto> completedRoutines) {
		return new RecordsDto(
			completedRoutines
		);
	}
}
