package tokyo.t6sdl.dancerscareer2019.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;

public class RegisteredEmailValidator implements ConstraintValidator<RegisteredEmail, String> {
	private final AccountRepository accountRepository;
	
	public RegisteredEmailValidator(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == "" || email == null) {
			return true;
		}
		return accountRepository.findOneByEmail(email) instanceof Account;
	}

}
