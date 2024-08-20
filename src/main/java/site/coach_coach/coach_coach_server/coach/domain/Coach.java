package site.coach_coach.coach_coach_server.coach.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.review.domain.Review;
import site.coach_coach.coach_coach_server.sport.domain.CoachingSport;
import site.coach_coach.coach_coach_server.user.domain.User;

@Entity
@Table(name = "coaches")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Coach extends DateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coach_id")
	private Long coachId;

	@NotNull
	@OneToOne
	@JoinColumn(name = "user_Id")
	private User user;

	@Lob
	@NotNull
	@Column(name = "coach_introduction")
	private String coachIntroduction;

	@Size(max = 100)
	@Column(name = "active_center")
	private String activeCenter;

	@Size(max = 100)
	@Column(name = "active_center_detail")
	private String activeCenterDetail;

	@Size(max = 100)
	@NotNull
	@Column(name = "active_hours")
	private String activeHours;

	@Size(max = 400)
	@NotNull
	@Column(name = "chatting_url")
	private String chattingUrl;

	@NotNull
	@Column(name = "is_open")
	private Boolean isOpen;

	@OneToMany(mappedBy = "coach")
	private List<CoachingSport> coachingSports;

	@OneToMany(mappedBy = "coach")
	private List<Review> reviews;

	public void update(String coachIntroduction, String activeCenter, String activeCenterDetail,
		String activeHours, String chattingUrl, Boolean isOpen) {
		this.coachIntroduction = coachIntroduction;
		this.activeCenter = activeCenter;
		this.activeCenterDetail = activeCenterDetail;
		this.activeHours = activeHours;
		this.chattingUrl = chattingUrl;
		this.isOpen = isOpen;
	}

	public Coach(User user) {
		this.user = user;
		this.coachIntroduction = "";
		this.activeCenter = "";
		this.activeCenterDetail = "";
		this.activeHours = "";
		this.chattingUrl = "";
		this.isOpen = true;
	}
}
