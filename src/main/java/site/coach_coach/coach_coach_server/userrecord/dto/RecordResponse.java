package site.coach_coach.coach_coach_server.userrecord.dto;

import java.time.LocalDate;

public record RecordResponse(
	Long recordId,
	LocalDate recordDate,
	boolean isCompleted
) {
}
