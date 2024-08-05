package site.coach_coach.coach_coach_server.auth.userdetails;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String userId) {
		Optional<User> user = userRepository.findById(Long.parseLong(userId));
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(userId);
		}

		return new CustomUserDetails(user.get());
	}
}
