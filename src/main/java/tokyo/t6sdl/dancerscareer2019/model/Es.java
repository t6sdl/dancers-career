package tokyo.t6sdl.dancerscareer2019.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Es {
	private int expId;
	private int id;
	private String corp;
	private String result;
	private List<String> question = new ArrayList<String>();
	private List<String> answer = new ArrayList<String>();
	private List<String> advice = new ArrayList<String>();
	
	@Override
	public String toString() {
		return this.getCorp() + this.getResult() + this.getQuestion().get(0) + this.getAnswer().get(0) + this.getAdvice().get(0);
	}
}
