package tokyo.t6sdl.dancerscareer.model.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import tokyo.t6sdl.dancerscareer.validation.RegisteredEmail;

@Data
public class EmailForm {
	@NotEmpty
	@Email
	@RegisteredEmail
	private String email;
}
