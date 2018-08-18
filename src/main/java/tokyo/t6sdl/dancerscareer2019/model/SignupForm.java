package tokyo.t6sdl.dancerscareer2019.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import tokyo.t6sdl.dancerscareer2019.validation.SamePassword;
import tokyo.t6sdl.dancerscareer2019.validation.UniqueEmail;

@Data
@SamePassword
public class SignupForm {
	@NotEmpty
	@Email
	@UniqueEmail
	private String email;
	@Size(min=8)
	@Pattern(message="半角英数字と記号（.-+_のうち少なくとも１つ）を必ず含めてください", regexp="^(?=[a-zA-Z0-9]*(\\.|\\-|\\+|_))(?=([a-zA-Z]|\\.|\\-|\\+|_)*[0-9])(?=([0-9]|\\.|\\-|\\+|_)*[a-zA-Z])(\\w|\\.|\\-|\\+|_)*$")
	private String password;
	private String confirmPassword;
}