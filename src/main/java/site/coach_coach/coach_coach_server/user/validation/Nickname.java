package site.coach_coach.coach_coach_server.user.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

@Documented
@Constraint(validatedBy = NicknameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nickname {
	String REGEX =
		"^(?!.*\\s{2,})[0-9a-zA-Z가-힣][0-9a-zA-Z가-힣\\s]{0,8}[0-9a-zA-Z가-힣]$";

	String message() default ErrorMessage.INVALID_NICKNAME;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
