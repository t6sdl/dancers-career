package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.Objects;

import lombok.Data;

@Data
public class EsForm {
	private String corp;
	private String result;
	private String question;
	private String answer;
	private String advice;
	
	@Override
	public String toString() {
		if (Objects.equals(this.getCorp(), null)) {
			this.setCorp("");
		}
		if (Objects.equals(this.getResult(), null)) {
			this.setResult("");
		}
		if (Objects.equals(this.getQuestion(), null)) {
			this.setQuestion("");
		}
		if (Objects.equals(this.getAnswer(), null)) {
			this.setAnswer("");
		}
		if (Objects.equals(this.getAdvice(), null)) {
			this.setAdvice("");
		}
		return this.getCorp() + this.getResult() + this.getQuestion() + this.getAnswer() + this.getAdvice();
	}
}
