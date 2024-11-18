package site.coach_coach.coach_coach_server.notification.service;

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
			.map(this::buildNotificationResponse)
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

		Notification notification = Notification.builder()
			.user(user)
			.coach(coach)
			.message(message)
			.relationFunction(relationFunction)
			.isRead(false)
			.build();
		notificationRepository.save(notification);
	}

	@Transactional
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
		notificationRepository.deleteAll(user.getNotifications());
	}

	@Transactional
	public void markNotificationAsRead(Long userId, Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_NOTIFICATION));

		if (!notification.getUser().getUserId().equals(userId)) {
			throw new AccessDeniedException();
		}
		notification.markAsRead();
		notificationRepository.save(notification);
	}

	@Transactional
	public void markAllNotificationsAsRead(Long userId) {
		List<Notification> unreadNotifications = notificationRepository.findUnreadNotificationsByUserId(userId);
		unreadNotifications.forEach(Notification::markAsRead);
		notificationRepository.saveAll(unreadNotifications);
	}

	private String createMessage(User user, User coach, RelationFunctionEnum relationFunction) {
		return switch (relationFunction) {
			case ask -> String.format(
				"%s%s%s", user.getNickname(), NotificationMessage.USER_MESSAGE.getMessage(),
				NotificationMessage.ASK_MESSAGE.getMessage()
			);
			case like -> String.format(
				"%s%s%s", user.getNickname(), NotificationMessage.USER_MESSAGE.getMessage(),
				NotificationMessage.LIKE_MESSAGE.getMessage()
			);
			case review -> NotificationMessage.REVIEW_MESSAGE.getMessage();
			case match -> String.format(
				"%s%s%s", coach.getNickname(), NotificationMessage.USER_MESSAGE.getMessage(),
				NotificationMessage.MATCH_MESSAGE.getMessage()
			);
			case refusal -> String.format(
				"%s%s%s", coach.getNickname(), NotificationMessage.USER_MESSAGE.getMessage(),
				NotificationMessage.REFUSAL_MESSAGE.getMessage()
			);
			case cancel -> String.format(
				"%s%s%s", coach.getNickname(), NotificationMessage.USER_MESSAGE.getMessage(),
				NotificationMessage.CANCEL_MESSAGE.getMessage()
			);
			case routine -> String.format(
				"%s%s%s", coach.getNickname(), NotificationMessage.USER_MESSAGE.getMessage(),
				NotificationMessage.ROUTINE_MESSAGE.getMessage()
			);
			case request -> coach.getNickname() + NotificationMessage.MATCH_REQUEST_MESSAGE.getMessage();
		};
	}

	private NotificationListResponse buildNotificationResponse(Notification notification) {
		User sender;
		if (isCoachSender(notification.getRelationFunction())) {
			sender = notification.getCoach();
		} else {
			sender = notification.getUser();
		}
		return NotificationListResponse.builder()
			.notificationId(notification.getNotificationId())
			.nickname(sender.getNickname())
			.profileImageUrl(sender.getProfileImageUrl())
			.message(notification.getMessage())
			.relationFunction(notification.getRelationFunction())
			.isRead(notification.isRead())
			.createdAt(notification.getCreatedAt())
			.build();
	}

	private boolean isCoachSender(RelationFunctionEnum relationFunction) {
		return switch (relationFunction) {
			case match, refusal, cancel, request, routine -> true;
			default -> false;
		};
	}
}
