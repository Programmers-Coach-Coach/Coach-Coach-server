package site.coach_coach.coach_coach_server.sport.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.sport.domain.Sport;

public interface SportRepository extends JpaRepository<Sport, Long> {
	Optional<Sport> findBySportName(String sportName);
}
