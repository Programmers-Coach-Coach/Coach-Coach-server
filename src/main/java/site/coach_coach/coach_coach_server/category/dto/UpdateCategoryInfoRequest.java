package site.coach_coach.coach_coach_server.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record UpdateCategoryInfoRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 45)
	String categoryName
) {
}
