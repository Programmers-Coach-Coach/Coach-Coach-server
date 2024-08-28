package site.coach_coach.coach_coach_server.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import site.coach_coach.coach_coach_server.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryIdAndRoutine_RoutineIdIsNotNull(Long categoryId);

	@Modifying
	@Query("UPDATE Category c SET c.isCompleted = FALSE")
	void resetIsCompleted();
}
