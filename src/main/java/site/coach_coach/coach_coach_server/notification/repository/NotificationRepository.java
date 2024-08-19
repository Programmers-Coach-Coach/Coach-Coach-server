package site.coach_coach.coach_coach_server.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.coach_coach.coach_coach_server.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
