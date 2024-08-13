package site.coach_coach.coach_coach_server.coach.domain;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.sport.domain.CoachingSport;
import site.coach_coach.coach_coach_server.user.domain.User;

import java.util.List;

@Entity
@Table(name = "coaches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coach extends DateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coach_id")
	private Long coachId;

	@OneToOne
	@JoinColumn(name = "userId")
	private User user;

	@Lob
	@NotBlank
	@Column(name = "coach_introduction")
	private String coachIntroduction;

	@Size(max = 100)
	@Column(name = "active_center")
	private String activeCenter;

	@Size(max = 100)
	@Column(name = "active_center_detail")
	private String activeCenterDetail;

	@NotBlank
	@Size(max = 100)
	@NotBlank
	@Column(name = "active_hours")
	private String activeHours;

	@Size(max = 400)
	@NotBlank
	@Column(name = "chatting_url")
	private String chattingUrl;

	@NotNull
	@Column(name = "is_open")
	private Boolean isOpen;

	@OneToMany(mappedBy = "coach")
	private List<CoachingSport> coachingSports;
}
