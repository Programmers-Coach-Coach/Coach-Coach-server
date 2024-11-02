package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record UpdateRoutineInfoRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 45)
	String routineName,

	@NotNull
	Long sportId,

	@NotNull
	String[] repeats,

	List<ActionDto> actions
) {
}
