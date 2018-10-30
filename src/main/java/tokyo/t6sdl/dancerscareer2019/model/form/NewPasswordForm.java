package tokyo.t6sdl.dancerscareer2019.model.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import tokyo.t6sdl.dancerscareer2019.validation.SamePassword;

@Data
@SamePassword
public class NewPasswordForm {
	private String currentPassword;
	@Size(min=8)
	@Pattern(message="半角英数字、記号（!.?+$%#&*=@）をそれぞれ最低1文字ずつ含む必要があります", regexp="^(?=[a-zA-Z0-9]*(\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@))(?=([a-zA-Z]|\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@)*[0-9])(?=([0-9]|\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@)*[a-zA-Z])(\\w|\\.|!|\\?|\\+|\\$|%|#|&|\\*|=|@)*$")
	private String newPassword;
	private String confirm;
}
