package site.coach_coach.coach_coach_server.completedcategory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.category.repository.CategoryRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.DuplicateValueException;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;
import site.coach_coach.coach_coach_server.completedcategory.repository.CompletedCategoryRepository;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;
import site.coach_coach.coach_coach_server.userrecord.service.UserRecordService;

@Service
@RequiredArgsConstructor
public class CompletedCategoryService {
	private final CompletedCategoryRepository completedCategoryRepository;
	private final UserRecordService userRecordService;
	private final CategoryRepository categoryRepository;

	private Category validateAccessToCategory(Long categoryId, Long userIdByJwt) {
		Category category = categoryRepository.findByCategoryIdAndRoutine_RoutineIdIsNotNull(categoryId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_CATEGORY));

		if (!category.getRoutine().getUser().getUserId().equals(userIdByJwt)) {
			throw new AccessDeniedException();
		}
		return category;
	}

	@Transactional
	public Long createCompletedCategory(Long categoryId, Long userIdByJwt) {
		Category category = validateAccessToCategory(categoryId, userIdByJwt);

		UserRecord userRecord = userRecordService.getUserRecordForCompleteCategory(userIdByJwt);
		completedCategoryRepository.findByUserRecord_RecordDateAndCategory_CategoryId(
				userRecord.getRecordDate(), category.getCategoryId())
			.ifPresent(completedCategory -> {
				throw new DuplicateValueException(ErrorMessage.DUPLICATE_COMPLETED_CATEGORY);
			});

		category.changeIsCompleted();

		CompletedCategory completedCategory = CompletedCategory.builder()
			.userRecord(userRecord)
			.category(category)
			.recordDate(userRecord.getRecordDate())
			.build();
		return completedCategoryRepository.save(completedCategory).getCompletedCategoryId();
	}

	@Transactional
	public void deleteCompletedCategory(Long categoryId, Long userIdByJwt) {
		Category category = validateAccessToCategory(categoryId, userIdByJwt);
		UserRecord userRecord = userRecordService.getUserRecordForCompleteCategory(userIdByJwt);

		int deletedCount = completedCategoryRepository.deleteByUserRecord_UserRecordIdAndCategory_CategoryId(
			userRecord.getUserRecordId(),
			category.getCategoryId());
		if (deletedCount == 0) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_COMPLETED_CATEGORY);
		} else {
			category.changeIsCompleted();
		}
	}
}
