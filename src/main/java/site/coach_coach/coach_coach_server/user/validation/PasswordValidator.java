package site.coach_coach.coach_coach_server.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

public class PasswordValidator implements ConstraintValidator<Password, String> {
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null || password.isBlank()) {
			return addErrorMessage(context, ErrorMessage.EMPTY_PASSWORD.getMessage());
		}

		if (!password.matches(Password.REGEX)) {
			return addErrorMessage(context, ErrorMessage.INVALID_PASSWORD.getMessage());
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
