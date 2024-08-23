package site.coach_coach.coach_coach_server.coach.dto;

import lombok.Builder;

@Builder
public record MatchingUserResponseDto(
	Long userId,
	String userName,
	String profileImageUrl,
	boolean isMatching

) {
}
