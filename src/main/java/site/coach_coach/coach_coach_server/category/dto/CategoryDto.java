package site.coach_coach.coach_coach_server.category.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.action.domain.Action;

public record CategoryDto(
	@NotNull
	Long categoryId,

	@NotNull
	String categoryName,

	List<Action> actionList
) {
}
