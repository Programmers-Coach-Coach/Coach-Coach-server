package site.coach_coach.coach_coach_server.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.common.exception.AccessDeniedException;
import site.coach_coach.coach_coach_server.common.exception.InvalidInputException;
import site.coach_coach.coach_coach_server.common.exception.NotFoundException;
import site.coach_coach.coach_coach_server.common.exception.UserNotFoundException;
import site.coach_coach.coach_coach_server.notification.constants.NotificationMessage;
import site.coach_coach.coach_coach_server.notification.domain.Notification;
import site.coach_coach.coach_coach_server.notification.dto.NotificationListResponse;
import site.coach_coach.coach_coach_server.notification.repository.NotificationRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
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
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

		return user.getNotifications()
			.stream()
			.map(NotificationListResponse::from)
			.toList();
	}

	@Transactional
	public void createNotification(Long userId, Long coachId, RelationFunctionEnum relationFunction) {
		User user = userRepository.findById(userId)
			.orElseThrow(UserNotFoundException::new);
		User coach = coachRepository.findUserByCoachId(coachId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_COACH));

		String message = createMessage(user, coach, relationFunction);
		if (message.isEmpty()) {
			throw new InvalidInputException(ErrorMessage.INVALID_REQUEST);
		}

		User receiver = (relationFunction == RelationFunctionEnum.match) ? user : coach;

		Notification notification = Notification.builder()
			.user(receiver)
			.message(message)
			.relationFunction(relationFunction)
			.build();
		notificationRepository.save(notification);
	}

	public void deleteNotification(Long userId, Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_NOTIFICATION));

		if (!notification.getUser().getUserId().equals(userId)) {
			throw new AccessDeniedException();
		}
		notificationRepository.delete(notification);
	}

	public void deleteAllNotifications(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		List<Notification> notifications = new ArrayList<>(user.getNotifications());
		if (!notifications.isEmpty()) {
			notificationRepository.deleteAll(notifications);
		}
	}

	private String createMessage(User user, User coach, RelationFunctionEnum relationFunction) {
		return switch (relationFunction) {
			case ask -> user.getNickname() + NotificationMessage.USER_MESSAGE.getMessage()
				+ NotificationMessage.ASK_MESSAGE.getMessage();
			case like -> user.getNickname() + NotificationMessage.USER_MESSAGE.getMessage()
				+ NotificationMessage.LIKE_MESSAGE.getMessage();
			case review -> NotificationMessage.REVIEW_MESSAGE.getMessage();
			case match -> coach.getNickname() + NotificationMessage.USER_MESSAGE.getMessage()
				+ NotificationMessage.MATCH_MESSAGE.getMessage();
		};
	}
}
