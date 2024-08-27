package site.coach_coach.coach_coach_server.review.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.user.domain.User;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends DateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coach_id")
	private Coach coach;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull
	@Size(max = 1000)
	@Column(name = "contents")
	private String contents;

	@NotNull
	@Column(name = "stars")
	private int stars;

	public Long getUserId() {
		if (this.user == null) {
			return null;
		}
		return this.user.getUserId();
	}

	public String getUserNickname() {
		if (this.user == null) {
			return null;
		}
		return this.user.getNickname();
	}

	public void updateContents(String contents, int stars) {
		this.contents = contents;
		this.stars = stars;
	}
}
