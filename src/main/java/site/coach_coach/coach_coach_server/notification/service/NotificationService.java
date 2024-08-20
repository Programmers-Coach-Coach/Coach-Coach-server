package site.coach_coach.coach_coach_server.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.coach.repository.CoachRepository;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.domain.RelationFunctionEnum;
import site.coach_coach.coach_coach_server.common.exception.InvalidInputException;
import site.coach_coach.coach_coach_server.notification.constants.NotificationMessage;
import site.coach_coach.coach_coach_server.notification.domain.Notification;
import site.coach_coach.coach_coach_server.notification.dto.NotificationListResponse;
import site.coach_coach.coach_coach_server.notification.dto.NotificationRequest;
import site.coach_coach.coach_coach_server.notification.repository.NotificationRepository;
import site.coach_coach.coach_coach_server.user.domain.User;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.repository.UserRepository;

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
	public Long createNotification(Long userId, NotificationRequest notificationRequest) {
		User user = userRepository.findById(userId).orElseThrow(InvalidUserException::new);
		String message;
		Long coachId = notificationRequest.coachId();
		User coach = coachRepository.findUserByCoachId(coachId);
		if (notificationRequest.relationFunction() == RelationFunctionEnum.ask) {
			message =
				user.getNickname() + NotificationMessage.USER_MESSAGE.getMessage()
					+ NotificationMessage.ASK_MESSAGE.getMessage();
		} else if (notificationRequest.relationFunction() == RelationFunctionEnum.like) {
			message = user.getNickname() + NotificationMessage.USER_MESSAGE.getMessage()
				+ NotificationMessage.LIKE_MESSAGE.getMessage();
		} else if (notificationRequest.relationFunction() == RelationFunctionEnum.review) {
			message = NotificationMessage.REVIEW_MESSAGE.getMessage();
		} else {
			throw new InvalidInputException(ErrorMessage.INVALID_REQUEST);
		}

		Notification notification = Notification.builder()
			.user(coach)
			.message(message)
			.relationFunction(notificationRequest.relationFunction())
			.build();
		notificationRepository.save(notification);
		return notification.getNotificationId();
	}
}
