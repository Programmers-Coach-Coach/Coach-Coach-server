package site.coach_coach.coach_coach_server.sport.dto;


import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record CoachingSportDto(

	@NotBlank
	Long sportId,

	@NotBlank
	String sportName
) {
}
