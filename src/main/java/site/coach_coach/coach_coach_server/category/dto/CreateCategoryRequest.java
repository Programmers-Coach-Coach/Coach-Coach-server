package site.coach_coach.coach_coach_server.category.dto;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.common.validation.InputName;

public record CreateCategoryRequest(
	@NotNull
	@InputName
	String categoryName
) {
}
