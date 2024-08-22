package site.coach_coach.coach_coach_server.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUser_UserId(Long userId);
}
