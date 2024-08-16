package site.coach_coach.coach_coach_server.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public class ValidBooleanValidator implements ConstraintValidator<ValidBoolean, Boolean> {
	@Override
	public boolean isValid(Boolean value, ConstraintValidatorContext context) {
		if (value == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ErrorMessage.INVALID_VALUE)
				.addConstraintViolation();
			return false;
		}

		return true;
	}
}
