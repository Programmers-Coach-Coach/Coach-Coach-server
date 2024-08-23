package site.coach_coach.coach_coach_server.userrecord.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRecordCreateRequest(
	@NotBlank
	String recordDate,
	Double weight,
	Double skeletalMuscle,
	Double fatPercentage,
	Double bmi
) {
}
