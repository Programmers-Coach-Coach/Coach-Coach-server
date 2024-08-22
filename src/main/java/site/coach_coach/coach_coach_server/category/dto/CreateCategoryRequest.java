package site.coach_coach.coach_coach_server.category.dto;

import jakarta.validation.constraints.NotBlank;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public record CreateCategoryRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String categoryName
) {
}
