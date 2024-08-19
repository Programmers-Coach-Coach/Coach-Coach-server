package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

@Builder
public record CoachListDto(
	@NotNull
	Long coachId,
	@NotBlank
	String coachName,
	String localAddress,
	@Size(max = 500)
	String profileImageUrl,
	@NotNull
	String coachIntroduction,
	List<CoachingSportDto> coachingSports,
	int countOfReviews,
	double reviewRating,
	boolean isLiked,
	int countOfLikes
) {
}
