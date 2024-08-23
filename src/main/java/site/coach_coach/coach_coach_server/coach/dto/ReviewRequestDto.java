package site.coach_coach.coach_coach_server.coach.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewRequestDto(
	String contents,

	@Min(1) @Max(5)
	int stars
) {
}
