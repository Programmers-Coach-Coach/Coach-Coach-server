package site.coach_coach.coach_coach_server.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.notification.dto.NotificationResponse;
import site.coach_coach.coach_coach_server.notification.repository.NotificationRepository;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;

	@Transactional(readOnly = true)
	public List<NotificationResponse> getAllNotifications(Long userId) {
		userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		return notificationRepository.findByUser_UserId(userId)
			.stream()
			.map(NotificationResponse::from)
			.toList();
	}
}
