package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ExperienceForm {
	private String lastName;
	private String firstName;
	private String kanaLastName;
	private String kanaFirstName;
	private String sex;
	private String major;
	@NotEmpty
	private String univPref;
	@NotEmpty
	private String univName;
	private String faculty;
	private String department;
	private String gradSchoolPref;
	private String gradSchoolName;
	private String gradSchoolOf;
	private String programIn;
	private String graduation;
	private String academicDegree;
	@NotEmpty
	private List<String> position;
	private List<String> club;
	private List<String> offer;
	private List<EsForm> es;
	private List<InterviewForm> interview;
	
	public void init() {
		List<String> emptyStr = new ArrayList<String>(Arrays.asList(""));
		List<EsForm> emptyEs = new ArrayList<EsForm>(Arrays.asList(new EsForm()));
		List<InterviewForm> emptyInterview = new ArrayList<InterviewForm>(Arrays.asList(new InterviewForm()));
		this.setClub(emptyStr);
		this.setOffer(emptyStr);
		this.setEs(emptyEs);
		this.setInterview(emptyInterview);
	}
}
