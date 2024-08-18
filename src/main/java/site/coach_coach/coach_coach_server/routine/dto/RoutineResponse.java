package site.coach_coach.coach_coach_server.routine.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import site.coach_coach.coach_coach_server.category.dto.CategoryDto;

public record RoutineResponse(
	@NotNull
	String routineName,

	List<CategoryDto> categoryList

) {
}
