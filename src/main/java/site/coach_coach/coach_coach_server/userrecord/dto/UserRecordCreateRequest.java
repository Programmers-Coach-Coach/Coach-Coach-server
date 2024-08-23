package site.coach_coach.coach_coach_server.userrecord.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

@Builder
public record UserRecordCreateRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String recordDate,
	Double weight,
	Double skeletalMuscle,
	Double fatPercentage,
	Double bmi
) {
}
