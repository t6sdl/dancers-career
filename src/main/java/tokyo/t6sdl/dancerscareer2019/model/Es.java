package tokyo.t6sdl.dancerscareer2019.model;

import java.util.List;

import lombok.Data;

@Data
public class Es {
	private int experience_id;
	private int es_id;
	private String corp;
	private String result;
	private List<BoundEs> content;
	
	@Override
	public String toString() {
		return this.getCorp() + this.getResult() + this.getContent().get(0).toString();
	}
}
