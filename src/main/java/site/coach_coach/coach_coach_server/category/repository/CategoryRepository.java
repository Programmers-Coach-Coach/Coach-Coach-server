package site.coach_coach.coach_coach_server.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.coach_coach.coach_coach_server.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryIdAndRoutine_RoutineIdIsNotNull(Long categoryId);

	@Modifying
	@Query("UPDATE Category c SET c.isCompleted = FALSE WHERE c.routine.routineId IS NOT NULL")
	void resetIsCompleted();

	@Modifying
	@Query("UPDATE Category c SET c.isDeleted = TRUE WHERE c.routine.routineId = :routineId")
	void changeIsDeletedByRemoveRoutine(@Param("routineId") Long routineId);

	@Query("SELECT c FROM Category c WHERE c.categoryId = :categoryId AND c.isDeleted IS FALSE "
		+ "AND c.routine.routineId IS NOT NULL")
	Optional<Category> findExistCategory(@Param("categoryId") Long categoryId);
}
