package site.coach_coach.coach_coach_server.coach.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.coach.domain.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {

	@Query("SELECT c.coachId FROM Coach c WHERE c.user.userId = :userId")
	Optional<Long> findCoachIdByUserId(@Param("userId") Long userId);

	@Query("SELECT c FROM Coach c "
		+ "LEFT JOIN c.coachingSports cs "
		+ "LEFT JOIN c.reviews r "
		+ "WHERE (:sports IS NULL OR cs.sport.sportId IN :sports) "
		+ "AND (:search IS NULL OR c.user.nickname LIKE %:search%) "
		+ "GROUP BY c.coachId")
	Page<Coach> findAllWithFilters(@Param("sports") List<Long> sports,
		@Param("search") String search,
		Pageable pageable);

	@Query("SELECT c FROM Coach c "
		+ "LEFT JOIN c.coachingSports cs "
		+ "LEFT JOIN c.reviews r "
		+ "WHERE (:sports IS NULL OR cs.sport.sportId IN :sports) "
		+ "AND (:search IS NULL OR c.user.nickname LIKE %:search%) "
		+ "GROUP BY c.coachId "
		+ "ORDER BY COUNT(r.reviewId) DESC")
	Page<Coach> findAllWithReviewsSorted(@Param("sports") List<Long> sports,
		@Param("search") String search,
		Pageable pageable);

	@Query("SELECT c FROM Coach c "
		+ "LEFT JOIN c.coachingSports cs "
		+ "LEFT JOIN c.reviews r "
		+ "WHERE (:sports IS NULL OR cs.sport.sportId IN :sports) "
		+ "AND (:search IS NULL OR c.user.nickname LIKE %:search%) "
		+ "GROUP BY c.coachId "
		+ "ORDER BY (SELECT COUNT(ucl) FROM UserCoachLike ucl WHERE ucl.coach.coachId = c.coachId) DESC")
	Page<Coach> findAllWithLikesSorted(@Param("sports") List<Long> sports,
		@Param("search") String search,
		Pageable pageable);

	@Query("SELECT c FROM Coach c "
		+ "JOIN UserCoachLike ucl ON ucl.coach = c "
		+ "WHERE ucl.user.userId = :userId")
	Page<Coach> findMyCoaches(@Param("userId") Long userId, Pageable pageable);
}
