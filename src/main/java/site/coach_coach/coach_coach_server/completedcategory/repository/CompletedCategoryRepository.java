package site.coach_coach.coach_coach_server.completedcategory.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;

public interface CompletedCategoryRepository extends JpaRepository<CompletedCategory, Long> {
	List<CompletedCategory> findAllByUserRecord_UserRecordId(Long recordId);

	Optional<CompletedCategory> findByUserRecord_RecordDateAndCategory_CategoryId(LocalDate recordDate,
		Long categoryId);

	int deleteByUserRecord_UserRecordIdAndCategory_CategoryId(Long userRecordId, Long categoryId);
}
