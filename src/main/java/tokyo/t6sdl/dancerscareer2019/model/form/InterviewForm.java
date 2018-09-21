package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.Objects;

import lombok.Data;

@Data
public class InterviewForm {
	private int interviewId;
	private String question;
	private String answer;
	
	@Override
	public String toString() {
		if (Objects.equals(this.getQuestion(), null)) {
			this.setQuestion("");
		}
		if (Objects.equals(this.getAnswer(), null)) {
			this.setAnswer("");
		}
		return this.getQuestion() + this.getAnswer();
	}
}
