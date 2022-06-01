package tokyo.t6sdl.dancerscareer.model.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import tokyo.t6sdl.dancerscareer.validation.UniqueEmail;

@Data
public class NewEmailForm {
	private String currentPassword;
	@NotEmpty
	@Email
	@UniqueEmail
	private String newEmail;
}
