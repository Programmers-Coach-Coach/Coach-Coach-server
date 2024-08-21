package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RoutineForListDto(
	@NotNull
	Long routineId,

	@NotBlank
	String routineName,

	@NotBlank
	String sportName
) {

}
