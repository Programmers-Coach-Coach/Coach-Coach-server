package site.coach_coach.coach_coach_server.user.dto;

import lombok.Builder;
import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.user.domain.User;

@Builder
public record UserDto(
	Long userId,
	String email,
	String nickname,
	String profileImageUrl,
	GenderEnum gender,
	String localAddress,
	String localAddressDetail,
	String introduction
) {
	public static UserDto from(User user) {
		return UserDto.builder()
			.userId(user.getUserId())
			.email(user.getEmail())
			.nickname(user.getNickname())
			.profileImageUrl(user.getProfileImageUrl())
			.gender(user.getGender())
			.localAddress(user.getLocalAddress())
			.localAddressDetail(user.getLocalAddressDetail())
			.introduction(user.getIntroduction())
			.build();
	}
}
