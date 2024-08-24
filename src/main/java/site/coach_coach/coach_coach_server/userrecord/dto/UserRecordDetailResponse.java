package site.coach_coach.coach_coach_server.userrecord.dto;

import java.util.List;

public record UserRecordDetailResponse(
	Double weight,
	Double skeletalMuscle,
	Double fatPercentage,
	Double bmi,
	List<RecordsDto> records
) {
	// public static UserRecordDetailResponse from(
	// 	UserRecord userRecord,
	// 	List<Routine> routines
	// ) {
	// 	List<RecordsDto> records = routines.stream()
	// 		.map(routine -> new RecordsDto(
	// 			routine.getCoachId(),
	// 			routine.getCoa
	// 		))
	// 		.collect(Collectors.toList());
	// }
}
