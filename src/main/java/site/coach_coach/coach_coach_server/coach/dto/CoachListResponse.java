package site.coach_coach.coach_coach_server.coach.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record CoachListResponse(
	List<CoachListDto> data,
	@NotNull
	int totalCount,
	@NotNull
	int currentPage
) {
}
