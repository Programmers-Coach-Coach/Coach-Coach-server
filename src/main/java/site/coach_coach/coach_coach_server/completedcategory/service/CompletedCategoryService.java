package site.coach_coach.coach_coach_server.completedcategory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.category.domain.Category;
import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;
import site.coach_coach.coach_coach_server.completedcategory.repository.CompletedCategoryRepository;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

@Service
@RequiredArgsConstructor
public class CompletedCategoryService {
	private final CompletedCategoryRepository completedCategoryRepository;
	private final UserRepository userRepository;

	@Transactional
	public Long createCompletedCategory(UserRecord userRecord, Category category) {
		CompletedCategory completedCategory = CompletedCategory.builder()
			.userRecord(userRecord)
			.category(category)
			.recordDate(userRecord.getRecordDate())
			.build();
		return completedCategoryRepository.save(completedCategory).getCompletedCategoryId();
	}

	@Transactional
	public void deleteCompletedCategory(UserRecord userRecord, Category category) {
		completedCategoryRepository.deleteByUserRecord_UserRecordIdAndCategory_CategoryId(
			userRecord.getUserRecordId(), category.getCategoryId());
	}
}
