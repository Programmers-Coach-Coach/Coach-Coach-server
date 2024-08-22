package site.coach_coach.coach_coach_server.recorddata.dto;

import lombok.Builder;

@Builder
public record RecordDataRequest(
	Double weight,
	Double skeletalMuscle,
	Double fatPercentage,
	Double bmi
) {
}
