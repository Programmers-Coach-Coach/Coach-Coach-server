package site.coach_coach.coach_coach_server.coach.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.review.dto.ReviewDto;
import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

@Builder
public record CoachDetailDto(
	@NotNull
	Long coachId,
	@NotNull
	String coachName,
	@NotNull
	GenderEnum coachGender,
	String localAddress,
	@NotNull
	String profileImageUrl,
	String createdAt,
	@NotNull
	String coachIntroduction,
	@NotNull
	List<CoachingSportDto> coachingSports,
	String activeCenter,
	String activeCenterDetail,
	@NotNull
	String activeHours,
	@NotNull
	@Size(max = 500)
	String chattingUrl,
	List<ReviewDto> reviews,
	@NotNull
	boolean isOpen,
	boolean isContacted,
	boolean isMatched,
	int countOfReviews,
	BigDecimal reviewRating,
	boolean isLiked,
	int countOfLikes
) {
}
