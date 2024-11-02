package site.coach_coach.coach_coach_server.userrecord.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.completedroutine.dto.CompletedRoutineDto;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

public record RecordsDto(
	Long coachId,
	String coachName,
	String coachProfileImageUrl,
	String routineName,
	List<CompletedRoutineDto> completedRoutines
) {
	public static RecordsDto from(Routine routine, Coach coach,
		List<CompletedRoutineDto> completedRoutines) {
		return new RecordsDto(
			coach != null ? coach.getCoachId() : null,
			coach != null ? coach.getUser().getNickname() : null,
			coach != null ? coach.getUser().getProfileImageUrl() : null,
			routine != null ? routine.getRoutineName() : null,
			completedRoutines
		);
	}
}
