package site.coach_coach.coach_coach_server.coach.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.coach.domain.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
	boolean existsByUserId(Long userId);

	Optional<Coach> findByUserId(Long userId);
}
