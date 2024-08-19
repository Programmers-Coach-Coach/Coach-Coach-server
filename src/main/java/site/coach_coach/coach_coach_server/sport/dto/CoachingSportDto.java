package site.coach_coach.coach_coach_server.sport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CoachingSportDto(
	@NotNull
	Long sportId,
	@NotBlank
	String sportName
) {
}
