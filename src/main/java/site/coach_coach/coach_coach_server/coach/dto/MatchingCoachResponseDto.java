package site.coach_coach.coach_coach_server.coach.dto;

public record MatchingCoachResponseDto(
	Long coachId,
	String coachName,
	String profileImageUrl,
	boolean isMatching
) {
}
