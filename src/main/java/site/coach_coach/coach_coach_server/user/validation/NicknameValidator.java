package site.coach_coach.coach_coach_server.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {
	@Override
	public boolean isValid(String nickname, ConstraintValidatorContext context) {
		if (nickname == null || nickname.isBlank()) {
			return addErrorMessage(context, ErrorMessage.EMPTY_NICKNAME);
		}

		if (!nickname.matches(Nickname.REGEX)) {
			return addErrorMessage(context, ErrorMessage.INVALID_NICKNAME);
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
