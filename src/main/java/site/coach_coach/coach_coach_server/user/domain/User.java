package site.coach_coach.coach_coach_server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;

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

	@Column(name = "nickname", unique = true, nullable = false, length = 45)
	private String nickname;

	@Column(name = "email", unique = true, nullable = false, length = 45)
	private String email;

	@Column(name = "password", nullable = false, length = 128)
	private String password;

	@Column(name = "profile_image_url", length = 400)
	private String profileImageUrl;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;

	@Column(name = "local_info", length = 200)
	private String localInfo;

	@Column(name = "introduction")
	private String introduction;
}
