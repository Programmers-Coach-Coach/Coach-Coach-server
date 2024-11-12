package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

public record MatchingCoachResponseDto(
	Long coachId,
	String coachName,
	String profileImageUrl,
	String localAddress,
	List<CoachingSportDto> coachingSports,
	boolean isLiked,
	boolean isMatching
) {
}
