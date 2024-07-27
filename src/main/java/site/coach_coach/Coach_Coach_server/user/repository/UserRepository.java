package site.coach_coach.Coach_Coach_server.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.Coach_Coach_server.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByNickname(String nickname);

	Optional<User> findByEmail(String email);
}
