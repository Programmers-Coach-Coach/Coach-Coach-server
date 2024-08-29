package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.category.dto.CategoryDto;
import site.coach_coach.coach_coach_server.routine.domain.Routine;

public record RoutineResponse(
	@NotNull
	String routineName,

	List<CategoryDto> categoryList

) {
	public static RoutineResponse from(Routine routine) {
		List<CategoryDto> categoryList = routine.getCategoryList().stream()
			.filter((category -> !category.isDeleted()))
			.map(CategoryDto::from)
			.collect(Collectors.toList());

		return new RoutineResponse(routine.getRoutineName(), categoryList);
	}
}
