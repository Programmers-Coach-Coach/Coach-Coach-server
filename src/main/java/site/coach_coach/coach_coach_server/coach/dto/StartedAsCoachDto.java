package site.coach_coach.coach_coach_server.coach.dto;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

public record StartedAsCoachDto(
	@NotNull(message = ErrorMessage.INVALID_VALUE)
	Long userId
) {
}
