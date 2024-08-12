package site.coach_coach.coach_coach_server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;

@Table(name = "users")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends DateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "nickname", unique = true, nullable = false, length = 45)
	@NotBlank
	@Size(max = 45)
	private String nickname;

	@Column(name = "email", unique = true, nullable = false, length = 45)
	@Email
	@NotBlank
	@Size(max = 45)
	private String email;

	@Column(name = "password", nullable = false, length = 128)
	@NotBlank
	@Size(max = 128)
	private String password;

	@Column(name = "profile_image_url", length = 400)
	@Size(max = 400)
	private String profileImageUrl;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;

	@Column(name = "local_info", length = 200)
	@Size(max = 200)
	private String localInfo;

	@Lob
	@Column(name = "introduction")
	private String introduction;

}
