package site.coach_coach.Coach_Coach_server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@Builder
@NoArgsConstructor
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

	public User(Long userId, String nickname, String email, String password) {
		this.userId = userId;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
	}

	public User(Long userId, String nickname, String email, String password,
		String profileImageUrl, String gender, String localInfo, String introduction) {
		this.userId = userId;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profileImageUrl = profileImageUrl;
		this.gender = gender;
		this.localInfo = localInfo;
		this.introduction = introduction;
	}

	public void updateUserInfo(String profileImageUrl, String gender, String localInfo, String introduction) {
		this.profileImageUrl = profileImageUrl;
		this.gender = gender;
		this.localInfo = localInfo;
		this.introduction = introduction;
	}
}
