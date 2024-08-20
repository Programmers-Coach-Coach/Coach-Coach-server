package site.coach_coach.coach_coach_server.notification.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.coach_coach.coach_coach_server.auth.userdetails.CustomUserDetails;
import site.coach_coach.coach_coach_server.notification.dto.NotificationListResponse;
import site.coach_coach.coach_coach_server.notification.dto.NotificationRequest;
import site.coach_coach.coach_coach_server.notification.dto.NotificationResponse;
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

	@PostMapping("/v1/notifications")
	public ResponseEntity<NotificationResponse> createNotification(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		NotificationRequest notificationRequest) {
		Long userId = userDetails.getUserId();
		Long notificationId = notificationService.createNotification(userId, notificationRequest);
		return ResponseEntity.ok(new NotificationResponse(HttpStatus.OK.value(), notificationId));
	}
}
