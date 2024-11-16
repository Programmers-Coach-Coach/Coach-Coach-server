package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import lombok.Builder;
import site.coach_coach.coach_coach_server.review.dto.ReviewDto;

@Builder
public record ReviewListDto(
	List<ReviewDto> reviews,
	int countOfReviews,
	double reviewRating,
	boolean isMatched,
	boolean isOpen
) {
}
