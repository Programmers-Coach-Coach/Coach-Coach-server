package site.coach_coach.coach_coach_server.userrecord.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.completedroutine.dto.CompletedRoutineDto;

public record UserRecordDetailV2Response(
	Long recordId,
	Double weight,
	Double skeletalMuscle,
	Double fatPercentage,
	Double bmi,
	List<CompletedRoutineDto> completedRoutines
) {
}
