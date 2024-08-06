package site.coach_coach.coach_coach_server.auth.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.auth.jwt.domain.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByRefreshToken(String token);

	Optional<RefreshToken> findByUserId(Long userId);

	boolean existsByRefreshToken(String token);
}
