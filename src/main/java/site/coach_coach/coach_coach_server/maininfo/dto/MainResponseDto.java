package site.coach_coach.coach_coach_server.maininfo.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.coach.dto.CoachDto;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;

public record MainResponseDto(
	List<SportDto> sports,
	List<CoachDto> coaches
) {
}
