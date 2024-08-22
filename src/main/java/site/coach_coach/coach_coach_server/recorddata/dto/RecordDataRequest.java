package site.coach_coach.coach_coach_server.recorddata.dto;

import lombok.Builder;

@Builder
public record RecordDataRequest(
	Integer weight,
	Integer skeletalMuscle,
	Integer fatPercentage,
	Double bmi
) {
}
