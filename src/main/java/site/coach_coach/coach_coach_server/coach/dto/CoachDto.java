package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.sport.dto.CoachingSportDto;

public record CoachDto(
	Long coachId,
	String coachName,
	String profileImageUrl,
	String description,
	int countOfLikes,
	boolean liked,
	List<CoachingSportDto> coachingSports
) {
}
