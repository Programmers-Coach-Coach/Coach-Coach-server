package site.coach_coach.coach_coach_server.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.common.exception.InvalidInputException;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.notification.constants.NotificationMessage;
import site.coach_coach.coach_coach_server.notification.domain.Notification;
import site.coach_coach.coach_coach_server.notification.dto.NotificationListResponse;
import site.coach_coach.coach_coach_server.notification.repository.NotificationRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final CoachRepository coachRepository;

	@Transactional(readOnly = true)
	public List<NotificationListResponse> getAllNotifications(Long userId) {
		boolean userExists = userRepository.existsById(userId);
		if (!userExists) {
			throw new InvalidUserException();
		}
		return notificationRepository.findByUser_UserId(userId)
			.stream()
			.map(NotificationListResponse::from)
			.toList();
	}

	@Transactional
	public void createNotification(Long userId, Long coachId, RelationFunctionEnum relationFunction) {
		User user = userRepository.findById(userId)
			.orElseThrow(InvalidUserException::new);
		User coach = coachRepository.findUserByCoachId(coachId)
			.orElseThrow(() -> new UserNotFoundException(ErrorMessage.NOT_FOUND_COACH));

		String message = createMessage(user, relationFunction);
		if (message.isEmpty()) {
			throw new InvalidInputException(ErrorMessage.INVALID_REQUEST);
		}

		Notification notification = Notification.builder()
			.user(coach)
			.message(message)
			.relationFunction(relationFunction)
			.build();
		notificationRepository.save(notification);
	}

	private String createMessage(User user, RelationFunctionEnum relationFunction) {
		return switch (relationFunction) {
			case ask -> user.getNickname() + NotificationMessage.USER_MESSAGE.getMessage()
				+ NotificationMessage.ASK_MESSAGE.getMessage();
			case like -> user.getNickname() + NotificationMessage.USER_MESSAGE.getMessage()
				+ NotificationMessage.LIKE_MESSAGE.getMessage();
			case review -> NotificationMessage.REVIEW_MESSAGE.getMessage();
		};
	}
}
