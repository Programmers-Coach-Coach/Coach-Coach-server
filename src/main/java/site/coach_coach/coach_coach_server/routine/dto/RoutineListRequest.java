package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotBlank;

public record RoutineListRequest(
	@NotBlank
	Long userId,

	@NotBlank
	Long coachId
) {

}
