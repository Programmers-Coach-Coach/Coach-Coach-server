package site.coach_coach.coach_coach_server.like.domain;

import jakarta.persistence.*;
import lombok.*;
import site.coach_coach.coach_coach_server.coach.domain.*;
import site.coach_coach.coach_coach_server.user.domain.*;

import java.time.*;

@Entity
@Table(name = "user_coach_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoachLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userCoachLikeId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "coach_id", nullable = false)
	private Coach coach;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

	private int recentLikes;
}
