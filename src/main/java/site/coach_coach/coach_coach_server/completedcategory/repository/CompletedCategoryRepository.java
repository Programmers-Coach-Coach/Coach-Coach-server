package site.coach_coach.coach_coach_server.completedcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.completedcategory.domain.CompletedCategory;

public interface CompletedCategoryRepository extends JpaRepository<CompletedCategory, Long> {
}
