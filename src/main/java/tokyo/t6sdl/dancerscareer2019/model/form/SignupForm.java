package tokyo.t6sdl.dancerscareer2019.model.form;

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
	private String newEmail;
	@Size(min=8)
	@Pattern(message="半角英数字、記号（!.?+$%#&*=@）をそれぞれ最低1文字ずつ含む必要があります", regexp="^(?=[a-zA-Z0-9]*(\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@))(?=([a-zA-Z]|\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@)*[0-9])(?=([0-9]|\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@)*[a-zA-Z])(\\w|\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@)*$")
	private String newPassword;
	private String confirm;
}