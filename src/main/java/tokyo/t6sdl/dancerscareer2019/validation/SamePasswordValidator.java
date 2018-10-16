package tokyo.t6sdl.dancerscareer2019.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class SamePasswordValidator implements ConstraintValidator<SamePassword, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(value);
		String password = beanWrapper.getPropertyValue("newPassword").toString();
		String confirmPassword = beanWrapper.getPropertyValue("confirm").toString();
		if (password.equals(confirmPassword)) {
			return true;
		} else {
			return false;
		}
	}
}