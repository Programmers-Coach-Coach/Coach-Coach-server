package site.coach_coach.coach_coach_server.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.coach_coach.coach_coach_server.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	int countByUser_UserIdAndIsReadFalse(Long userId);

	@Modifying
	@Query("UPDATE Notification n SET n.isRead = TRUE WHERE n.user.userId = :userId AND n.isRead = FALSE")
	void updateIsReadByUserID(@Param("userId") Long userId);
}
