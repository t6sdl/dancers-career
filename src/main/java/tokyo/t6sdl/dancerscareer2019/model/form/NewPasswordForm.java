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
	@Pattern(message="半角英数字と記号（.-+_のうち少なくとも１つ）を必ず含めてください", regexp="^(?=[a-zA-Z0-9]*(\\.|\\-|\\+|_))(?=([a-zA-Z]|\\.|\\-|\\+|_)*[0-9])(?=([0-9]|\\.|\\-|\\+|_)*[a-zA-Z])(\\w|\\.|\\-|\\+|_)*$")
	private String newPassword;
	private String confirm;
}
