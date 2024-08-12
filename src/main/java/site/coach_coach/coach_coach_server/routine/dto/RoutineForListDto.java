package site.coach_coach.coach_coach_server.routine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RoutineForListDto(
	@NotBlank
	Long routineId,

	@NotBlank
	@Size(max = 45)
	String routineName,

	@NotBlank
	@Size(max = 45)
	String sportName
) {

}
