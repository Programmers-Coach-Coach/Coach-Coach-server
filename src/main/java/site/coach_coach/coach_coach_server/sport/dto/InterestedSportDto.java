package site.coach_coach.coach_coach_server.sport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InterestedSportDto(
	@NotNull
	Long userId,

	@NotBlank
	String sportName
) {
}
