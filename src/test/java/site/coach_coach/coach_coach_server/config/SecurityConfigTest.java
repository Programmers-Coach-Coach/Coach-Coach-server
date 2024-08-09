package site.coach_coach.coach_coach_server.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("CORS 설정 테스트 - 유효한 Origin")
	public void corsConfigTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test")
				.header("Origin", "https://coach-coach.site"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", "https://coach-coach.site"))
			.andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Credentials", "true"));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test")
				.header("Origin", "http://localhost:5173"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(
				MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
			.andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Credentials", "true"));
	}

	@Test
	@DisplayName("CORS 설정 테스트 - 유효하지 않은 Origin")
	public void corsConfigInvalidOriginTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/test")
				.header("Origin", "https://unauthorized-origin.com"))
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
