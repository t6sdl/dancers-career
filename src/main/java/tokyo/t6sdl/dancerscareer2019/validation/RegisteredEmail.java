package tokyo.t6sdl.dancerscareer2019.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = RegisteredEmailValidator.class)
@Retention(RUNTIME)
@Target(FIELD)
public @interface RegisteredEmail {
	String message() default "{tokyo.t6sdl.dancerscareer2019.validation.RegisteredEmail.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
