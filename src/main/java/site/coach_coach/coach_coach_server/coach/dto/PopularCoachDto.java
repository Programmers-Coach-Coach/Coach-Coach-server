package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

public record PopularCoachDto(
	@NotNull
	Long coachId,
	@NotBlank
	String coachName,
	@Size(max = 500)
	String profileImageUrl,
	@NotBlank
	String description,
	int countOfLikes,
	boolean isLiked,
	List<CoachingSportDto> coachingSports
) {
}
