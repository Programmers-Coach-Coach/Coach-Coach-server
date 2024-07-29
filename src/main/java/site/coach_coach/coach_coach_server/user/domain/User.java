package site.coach_coach.coach_coach_server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	private String nickname;

	@Column(unique = true)
	private String email;
	private String password;

	@Column(name = "profile_image_url")
	private String profileImageUrl;
	private String gender;

	@Column(name = "local_info")
	private String localInfo;
	private String introduction;
}
