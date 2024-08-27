package site.coach_coach.coach_coach_server.coach.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ReviewRequestDto(
	@Size(max = 1000)
	String contents,
	@Min(1) @Max(5)
	int stars
) {
}
