package site.coach_coach.coach_coach_server.sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.sport.domain.InterestedSport;
import site.coach_coach.coach_coach_server.user.domain.User;

public interface InterestedSportRepository extends JpaRepository<InterestedSport, User> {
	void deleteByUser(User user);
}
