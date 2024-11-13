package site.coach_coach.coach_coach_server.maininfo.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.sport.dto.SportDto;

public record MainInfoResponseDto(
	List<SportDto> sports,
	List<MainInfoCoachDto> coaches
) {
}
