package site.coach_coach.coach_coach_server.userrecord.dto;

import java.util.List;

import site.coach_coach.coach_coach_server.completedcategory.dto.CompletedCategoryDto;

public record RecordsDto(
	Long coachId,
	String coachName,
	String coachProfileImageUrl,
	String routineName,
	List<CompletedCategoryDto> completedCategories
) {
}
