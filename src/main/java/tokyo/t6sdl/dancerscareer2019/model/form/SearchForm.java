package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.List;

import lombok.Data;

@Data
public class SearchForm {
	private String kanaLastName;
	private String kanaFirstName;
	private String univPref;
	private String univName;
	private String faculty;
	private String department;
	private String gradSchoolPref;
	private String gradSchoolName;
	private String gradSchoolOf;
	private String programIn;
	private List<String> position;
	private String sort;
	
	public SearchForm(String sort) {
		this.setSort(sort);
	}
	
	public void clean() {
		this.setKanaLastName(null);
		this.setKanaFirstName(null);
		this.setUnivPref(null);
		this.setUnivName(null);
		this.setFaculty(null);
		this.setDepartment(null);
		this.setPosition(null);
	}
}
