package site.coach_coach.coach_coach_server.user.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class SignUpRequestTest {
	private Validator validator;

	@BeforeEach
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 SignupDto 객체 검증")
	public void testValidSignupDto() {
		SignUpRequest dto = new SignUpRequest("test", "test@test.com", "password123!");
		var violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("유효하지 않은 SignupDto 객체 검증")
	public void testInvalidSignupDto() {
		SignUpRequest dto = new SignUpRequest("", "invalidEmail", "short");
		var violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
	}
}
