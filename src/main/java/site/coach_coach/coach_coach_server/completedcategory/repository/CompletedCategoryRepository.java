package site.coach_coach.coach_coach_server.completedcategory.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;

@Repository
public interface CompletedCategoryRepository extends JpaRepository<CompletedCategory, Long> {
	@Query("SELECT cc FROM CompletedCategory cc "
		+ "LEFT JOIN FETCH cc.category c "
		+ "LEFT JOIN FETCH c.routine r "
		+ "LEFT JOIN FETCH r.coach "
		+ "WHERE cc.userRecord.userRecordId = :recordId")
	List<CompletedCategory> findAllWithDetailsByUserRecordId(@Param("recordId") Long recordId);

	Optional<CompletedCategory> findByUserRecord_RecordDateAndCategory_CategoryId(LocalDate recordDate,
		Long categoryId);

	int deleteByUserRecord_UserRecordIdAndCategory_CategoryId(Long userRecordId, Long categoryId);
}
