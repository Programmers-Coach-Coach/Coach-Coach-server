package site.coach_coach.coach_coach_server.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import site.coach_coach.coach_coach_server.common.domain.GenderEnum;
import site.coach_coach.coach_coach_server.user.domain.User;

public class UserDtoTest {

	@Test
	@DisplayName("User 엔티티에서 UserDto로 변환")
	public void testFromUser() {
		User user = User.builder()
			.userId(1L)
			.nickname("test")
			.email("test@test.com")
			.password("password123!")
			.profileImageUrl("profileImageUrl")
			.gender(GenderEnum.valueOf("W"))
			.localAddress("localAddress")
			.localAddressDetail("localAddressDetail")
			.introduction("introduction")
			.build();

		UserDto userDto = UserDto.from(user);

		assertNotNull(userDto);
		assertEquals(1L, userDto.userId());
		assertEquals("test", userDto.nickname());
		assertEquals("test@test.com", userDto.email());
		assertEquals("profileImageUrl", userDto.profileImageUrl());
		assertEquals(GenderEnum.W, userDto.gender());
		assertEquals("localAddress", userDto.localAddress());
		assertEquals("localAddressDetail", userDto.localAddressDetail());
		assertEquals("introduction", userDto.introduction());
	}
}
