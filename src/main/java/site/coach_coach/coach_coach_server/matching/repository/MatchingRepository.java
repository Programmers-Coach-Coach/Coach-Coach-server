package site.coach_coach.coach_coach_server.matching.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.matching.Dto.CheckMatchingDto;
import site.coach_coach.coach_coach_server.matching.domain.Matching;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
	Optional<CheckMatchingDto> findByUserIdAndCoachId(Long userId, Long coachId);
}
