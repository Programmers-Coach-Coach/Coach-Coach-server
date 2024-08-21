package site.coach_coach.coach_coach_server.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

@Documented
@Constraint(validatedBy = InputNameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InputName {
	String message() default ErrorMessage.INVALID_VALUE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
