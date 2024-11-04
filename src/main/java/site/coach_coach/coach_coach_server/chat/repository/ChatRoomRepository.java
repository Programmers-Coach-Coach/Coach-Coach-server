package site.coach_coach.coach_coach_server.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.chat.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	List<ChatRoom> findByUser_UserId(Long userId);

	List<ChatRoom> findByCoach_CoachId(Long coachId);
}
