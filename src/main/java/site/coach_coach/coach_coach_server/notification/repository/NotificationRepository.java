package site.coach_coach.coach_coach_server.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.coach_coach.coach_coach_server.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	int countByUser_UserIdAndIsReadFalse(Long userId);

	@Query("SELECT n FROM Notification n WHERE n.user.userId = :userId AND n.isRead = false")
	List<Notification> findUnreadNotificationsByUserId(@Param("userId") Long userId);
}
