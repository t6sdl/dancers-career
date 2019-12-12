package tokyo.t6sdl.dancerscareer2019.model;

import lombok.Data;

@Data
public class Interview {
	private int expId;
	private int id;
	private String question;
	private String answer;
	
	@Override
	public String toString() {
		return this.getQuestion() + this.getAnswer();
	}
}
