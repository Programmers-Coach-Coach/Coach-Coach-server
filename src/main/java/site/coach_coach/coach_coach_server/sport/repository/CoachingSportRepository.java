package site.coach_coach.coach_coach_server.sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.sport.domain.CoachingSport;

public interface CoachingSportRepository extends JpaRepository<CoachingSport, Long> {
	void deleteByCoach(Coach coach);
}
