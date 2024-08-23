package site.coach_coach.coach_coach_server.userrecord.dto;

import java.util.List;

public record UserRecordResponse(
	List<RecordResponse> records
) {
}
