package site.coach_coach.coach_coach_server.sport.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sport_id")
	private Long sportId;

	@Column(name = "sport_name")
	private String sportName;

	@Column(name = "sport_image_url")
	private String sportImageUrl;
}
