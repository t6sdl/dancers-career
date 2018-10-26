package tokyo.t6sdl.dancerscareer2019.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BoundEs {
	private String question;
	private String answer;
	private String advice;
	
	public String toString() {
		return this.getQuestion() + this.getAnswer() + this.getAdvice();
	}
}
