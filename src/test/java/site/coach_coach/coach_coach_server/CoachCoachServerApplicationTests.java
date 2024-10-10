package site.coach_coach.coach_coach_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import site.coach_coach.coach_coach_server.auth.oauth.CustomOAuth2Handler;
import site.coach_coach.coach_coach_server.auth.oauth.CustomOAuth2UserService;

@SpringBootTest
class CoachCoachServerApplicationTests {
	@MockBean
	private CustomOAuth2UserService customOAuth2UserService;

	@MockBean
	private CustomOAuth2Handler customOAuth2Handler;

	@Test
	void contextLoads() {
	}

}
