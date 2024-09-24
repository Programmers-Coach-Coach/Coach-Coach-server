package site.coach_coach.coach_coach_server.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	User findByUsername(String username);
}
