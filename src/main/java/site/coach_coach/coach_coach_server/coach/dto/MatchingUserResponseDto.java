package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import lombok.Builder;
import site.coach_coach.coach_coach_server.sport.dto.InterestedSportDto;

@Builder
public record MatchingUserResponseDto(
	Long userId,
	String userName,
	String profileImageUrl,
	String localAddress,
	List<InterestedSportDto> interestedSports,
	String startDate,
	boolean isMatching
) {
}
