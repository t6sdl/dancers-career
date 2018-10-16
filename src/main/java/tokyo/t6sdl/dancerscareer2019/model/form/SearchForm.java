package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.List;

import lombok.Data;

@Data
public class SearchForm {
	private String kanaLastName;
	private String kanaFirstName;
	private String prefecture;
	private String university;
	private String faculty;
	private String department;
	private List<String> position;
	private String sort;
	
	public SearchForm(String sort) {
		this.setSort(sort);
	}
	
	public void clean() {
		this.setKanaLastName(null);
		this.setKanaFirstName(null);
		this.setPrefecture(null);
		this.setUniversity(null);
		this.setFaculty(null);
		this.setDepartment(null);
		this.setPosition(null);
	}
}
