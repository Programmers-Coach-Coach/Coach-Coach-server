package site.coach_coach.coach_coach_server.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.matching.Dto.CheckMathcingDto;
import site.coach_coach.coach_coach_server.matching.domain.Matching;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {
	CheckMathcingDto findByUserIdAndCoachId(Long userId, Long coachId);
}
