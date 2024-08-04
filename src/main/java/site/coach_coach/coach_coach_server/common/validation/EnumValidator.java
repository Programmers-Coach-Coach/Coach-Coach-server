package site.coach_coach.coach_coach_server.common.validation;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, String> {
	private Enum annotation;

	@Override
	public void initialize(Enum constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String type, ConstraintValidatorContext context) {
		if (type == null) {
			return annotation.nullable();
		}

		return Arrays.stream(this.annotation.enumClass().getEnumConstants())
			.anyMatch(en -> (en.name().equals(type)));
	}
}
