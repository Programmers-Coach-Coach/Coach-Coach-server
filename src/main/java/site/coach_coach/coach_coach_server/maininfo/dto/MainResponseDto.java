package site.coach_coach.coach_coach_server.maininfo.dto;

import site.coach_coach.coach_coach_server.coach.dto.CoachDto;
import site.coach_coach.coach_coach_server.sport.dto.SportDto;

import java.util.List;

public record MainResponseDto(
	List<SportDto> sports,
	List<CoachDto> coaches
) {
}
