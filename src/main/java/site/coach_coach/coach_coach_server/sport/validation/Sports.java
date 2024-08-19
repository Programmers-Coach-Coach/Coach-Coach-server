package site.coach_coach.coach_coach_server.sport.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

@Documented
@Constraint(validatedBy = SportsValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sports {
	String message() default ErrorMessage.NOT_FOUND_SPORTS;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
