package site.coach_coach.coach_coach_server.completedcategory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.category.service.CategoryService;
import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;
import site.coach_coach.coach_coach_server.completedcategory.repository.CompletedCategoryRepository;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;
import site.coach_coach.coach_coach_server.userrecord.service.UserRecordService;

@Service
@RequiredArgsConstructor
public class CompletedCategoryService {
	private final CompletedCategoryRepository completedCategoryRepository;
	private final CategoryService categoryService;
	private final UserRecordService userRecordService;

	@Transactional
	public Long createCompletedCategory(Long routineId, Long categoryId, Long userIdByJwt) {
		Category category = categoryService.changeIsCompleted(categoryId, routineId, true);
		UserRecord userRecord = userRecordService.getUserRecordForCompleteCategory(userIdByJwt);
		CompletedCategory completedCategory = CompletedCategory.builder()
			.userRecord(userRecord)
			.category(category)
			.recordDate(userRecord.getRecordDate())
			.build();
		return completedCategoryRepository.save(completedCategory).getCompletedCategoryId();

	}

	@Transactional
	public void deleteCompletedCategory(Long routineId, Long categoryId, Long userIdByJwt) {
		Category category = categoryService.changeIsCompleted(categoryId, routineId, false);
		UserRecord userRecord = userRecordService.getUserRecordForCompleteCategory(userIdByJwt);
		completedCategoryRepository.deleteByUserRecord_UserRecordIdAndCategory_CategoryId(
			userRecord.getUserRecordId(), category.getCategoryId());
	}
}
