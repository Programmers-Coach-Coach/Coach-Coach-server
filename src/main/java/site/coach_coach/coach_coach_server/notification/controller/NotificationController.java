package site.coach_coach.coach_coach_server.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.common.constants.SuccessMessage;
import site.coach_coach.coach_coach_server.common.response.SuccessResponse;
import site.coach_coach.coach_coach_server.notification.dto.NotificationListResponse;
import site.coach_coach.coach_coach_server.notification.service.NotificationService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping("/v1/notifications")
	public ResponseEntity<List<NotificationListResponse>> getAllNotifications(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUserId();
		List<NotificationListResponse> notifications = notificationService.getAllNotifications(userId);
		return ResponseEntity.ok(notifications);
	}

	@DeleteMapping("/v1/notifications")
	public ResponseEntity<SuccessResponse> deleteAllNotifications(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUserId();
		notificationService.deleteAllNotifications(userId);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.DELETE_NOTIFICATION_SUCCESS.getMessage())
		);
	}

	@DeleteMapping("/v1/notifications/{notificationId}")
	public ResponseEntity<SuccessResponse> deleteNotification(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable("notificationId") Long notificationId) {
		Long userId = customUserDetails.getUserId();
		notificationService.deleteNotification(userId, notificationId);
		return ResponseEntity.ok(
			new SuccessResponse(HttpStatus.OK.value(), SuccessMessage.DELETE_NOTIFICATION_SUCCESS.getMessage())
		);
	}
}
