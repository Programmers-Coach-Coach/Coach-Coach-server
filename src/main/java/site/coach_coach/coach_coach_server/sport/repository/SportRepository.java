package site.coach_coach.coach_coach_server.sport.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.coach_coach.coach_coach_server.sport.domain.Sport;

public interface SportRepository extends JpaRepository<Sport, Long> {
	Optional<Sport> findBySportName(String sportName);

	@Query("SELECT s.sportId FROM Sport s")
	List<Long> findAllSportIds();
}
