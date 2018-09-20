package tokyo.t6sdl.dancerscareer2019.model;

import lombok.Data;

@Data
public class Es {
	private int experience_id;
	private int es_id;
	private String corp;
	private String result;
	private String question;
	private String answer;
	private String advice;
	
	@Override
	public String toString() {
		return this.getCorp() + this.getResult() + this.getQuestion() + this.getAnswer() + this.getAdvice();
	}
}
