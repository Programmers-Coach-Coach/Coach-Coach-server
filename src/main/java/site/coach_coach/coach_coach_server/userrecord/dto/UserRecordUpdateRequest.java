package site.coach_coach.coach_coach_server.userrecord.dto;

public record UserRecordUpdateRequest(
	Double weight,
	Double skeletalMuscle,
	Double fatPercentage,
	Double bmi
) {
}
