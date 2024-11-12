package site.coach_coach.coach_coach_server.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.coach_coach.coach_coach_server.chat.domain.ChatRoom;
import site.coach_coach.coach_coach_server.user.domain.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
	List<ChatRoom> findByUser(User user);

	List<ChatRoom> findByCoach_CoachId(Long coachId);
}
