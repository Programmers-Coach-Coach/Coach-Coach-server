package site.coach_coach.Coach_Coach_server.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null || password.isBlank()) {
			return addErrorMessage(context, ErrorMessage.EMPTY_PASSWORD);
		}

		if (!password.matches(Password.REGEX)) {
			return addErrorMessage(context, ErrorMessage.INVALID_PASSWORD);
		}
		return true;
	}

	private boolean addErrorMessage(ConstraintValidatorContext context, String message) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(message)
			.addConstraintViolation();

		return false;
	}
}
