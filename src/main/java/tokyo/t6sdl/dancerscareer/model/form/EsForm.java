package tokyo.t6sdl.dancerscareer.model.form;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class EsForm {
	private int id;
	private String corp;
	private String result;
	@NotEmpty
	private String question;
	@NotEmpty
	private String answer;
	@NotEmpty
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
