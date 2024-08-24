package site.coach_coach.coach_coach_server.userrecord.dto;

import java.time.LocalDate;

public record BodyInfoChartResponse(
	LocalDate recordDate,
	Double value
) {
}
