package site.coach_coach.coach_coach_server.like.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.user.domain.User;


@Entity
@Table(name = "user_coach_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCoachLike extends DateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userCoachLikeId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "coach_id")
	private Coach coach;

	private int recentLikes;
}
