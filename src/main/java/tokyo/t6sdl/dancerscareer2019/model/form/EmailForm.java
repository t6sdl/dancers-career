package tokyo.t6sdl.dancerscareer2019.model.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import tokyo.t6sdl.dancerscareer2019.validation.RegisteredEmail;

@Data
public class EmailForm {
	@NotEmpty
	@Email
	@RegisteredEmail
	private String email;
}
