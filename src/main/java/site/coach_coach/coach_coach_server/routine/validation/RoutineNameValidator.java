package site.coach_coach.coach_coach_server.routine.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public class RoutineNameValidator implements ConstraintValidator<RoutineName, String> {
	@Override
	public boolean isValid(String routineName, ConstraintValidatorContext context) {
		if (routineName.isBlank()) {
			return addErrorMessage(context, ErrorMessage.INVALID_VALUE);
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
