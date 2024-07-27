package site.coach_coach.Coach_Coach_server.user.dto;

import lombok.Builder;
import site.coach_coach.Coach_Coach_server.user.domain.User;

@Builder
public record UserDto(
	Long userId,
	String email,
	String nickname,
	String profileImageUrl,
	String gender,
	String localInfo,
	String introduction
) {
	public static UserDto from(User user) {
		return UserDto.builder()
			.userId(user.getUserId())
			.email(user.getEmail())
			.nickname(user.getNickname())
			.profileImageUrl(user.getProfileImageUrl())
			.gender(user.getGender())
			.localInfo(user.getLocalInfo())
			.introduction(user.getIntroduction())
			.build();
	}
}
