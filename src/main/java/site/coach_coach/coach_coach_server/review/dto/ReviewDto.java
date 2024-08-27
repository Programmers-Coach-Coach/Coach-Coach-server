package site.coach_coach.coach_coach_server.review.dto;

import lombok.Builder;

@Builder
public record ReviewDto(
	Long reviewId,
	Long userId,
	String userName,
	String contents,
	int stars,
	String createdAt,
	Boolean isMyReview
) {
}
