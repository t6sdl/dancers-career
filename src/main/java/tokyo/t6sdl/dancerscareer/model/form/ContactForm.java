package tokyo.t6sdl.dancerscareer.model.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContactForm {
	private String from;
	private String content;

	public ContactForm(String from) {
		this.setFrom(from);
	}
}
