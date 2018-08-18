package tokyo.t6sdl.dancerscareer2019.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = SamePasswordValidator.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface SamePassword {
	String message() default "{tokyo.t6sdl.dancerscareer2019.validation.SamePassword.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
