package site.coach_coach.coach_coach_server.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.category.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
