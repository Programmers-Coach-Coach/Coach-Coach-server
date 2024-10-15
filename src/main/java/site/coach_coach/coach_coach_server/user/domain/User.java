package site.coach_coach.coach_coach_server.user.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.coach_coach.coach_coach_server.coach.domain.Coach;
import site.coach_coach.coach_coach_server.common.domain.DateEntity;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.common.domain.UserRole;
import site.coach_coach.coach_coach_server.notification.domain.Notification;
import site.coach_coach.coach_coach_server.sport.domain.InterestedSport;
import site.coach_coach.coach_coach_server.userrecord.domain.UserRecord;

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

	@Column(name = "password", length = 200)
	@Size(max = 200)
	private String password;

	@Column(name = "profile_image_url", length = 500)
	@Size(max = 500)
	private String profileImageUrl;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private GenderEnum gender;

	@Column(name = "local_address", length = 100)
	@Size(max = 100)
	private String localAddress;

	@Column(name = "local_address_detail", length = 100)
	@Size(max = 100)
	private String localAddressDetail;

	@NotNull
	@Column(name = "is_coach")
	private Boolean isCoach;

	@Size(max = 1000)
	@Column(name = "introduction")
	private String introduction;

	@Column(name = "role")
	@NotNull
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@NotNull
	@Column(name = "is_social")
	private Boolean isSocial;

	@Column(name = "username")
	@Size(max = 500)
	private String username;

	@OneToOne(mappedBy = "user")
	private Coach coach;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<InterestedSport> interestedSports;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Notification> notifications;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserRecord> userRecords;

	public void updateProfile(String nickname, String profileImageUrl, GenderEnum gender, String localAddress,
		String localAddressDetail, String introduction, List<InterestedSport> interestedSports) {
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.gender = gender;
		this.localAddress = localAddress;
		this.localAddressDetail = localAddressDetail;
		this.introduction = introduction;
		this.interestedSports = interestedSports;
	}

	public void signUpOAuth2(String nickname, String email, String username) {
		this.nickname = nickname;
		this.email = email;
		this.username = username;
		this.role = UserRole.ROLE_GUEST;
		this.isCoach = false;
		this.isSocial = true;
	}

	public void updateOAuth2User(String nickname) {
		this.nickname = nickname;
		this.role = UserRole.ROLE_USER;
	}

	public void promoteToCoach() {
		this.isCoach = true;
	}
}
