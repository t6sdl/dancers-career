package tokyo.t6sdl.dancerscareer.model.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContactForm {
	@NotEmpty
	@Email
	private String from;
	@NotEmpty
	private String content;

	public ContactForm(String from) {
		this.setFrom(from);
	}
}
