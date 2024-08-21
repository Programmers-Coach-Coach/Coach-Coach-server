package site.coach_coach.coach_coach_server.category.dto;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.category.domain.Category;

public record CategoryDto(
	@NotNull
	Long categoryId,

	@NotNull
	String categoryName,

	@NotNull
	Boolean isCompleted,

	List<ActionDto> actionList
) {
	public static CategoryDto from(Category category) {
		List<ActionDto> actionList = category.getActionList().stream()
			.map(ActionDto::from)
			.collect(Collectors.toList());

		return new CategoryDto(category.getCategoryId(), category.getCategoryName(), category.getIsCompleted(),
			actionList);
	}
}
