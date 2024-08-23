package site.coach_coach.coach_coach_server.completedcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;

public interface CompletedCategoryRepository extends JpaRepository<CompletedCategory, Long> {
	Boolean existsByUserRecord_UserRecordIdAndCategory_CategoryId(Long userRecordId, Long categoryId);

	void deleteByUserRecord_UserRecordIdAndCategory_CategoryId(Long userRecordId, Long categoryId);
}
