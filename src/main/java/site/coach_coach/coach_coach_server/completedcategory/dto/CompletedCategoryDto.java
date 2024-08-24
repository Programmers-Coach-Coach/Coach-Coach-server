package site.coach_coach.coach_coach_server.completedcategory.dto;

import java.util.List;
import java.util.stream.Collectors;

import site.coach_coach.coach_coach_server.action.dto.ActionDto;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;

public record CompletedCategoryDto(
	Long categoryId,
	String categoryName,
	List<ActionDto> actions
) {
	public static CompletedCategoryDto from(CompletedCategory completedCategory) {
		Category category = completedCategory.getCategory();
		List<ActionDto> actions = category.getActionList().stream()
			.map(ActionDto::from)
			.collect(Collectors.toList());

		return new CompletedCategoryDto(
			category.getCategoryId(),
			category.getCategoryName(),
			actions
		);
	}
}
