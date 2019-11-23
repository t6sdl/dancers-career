package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ExperienceForm {
	private String familyName;
	private String givenName;
	private String kanaFamilyName;
	private String kanaGivenName;
	private String sex;
	private String major;
	@NotEmpty
	private String univLoc;
	@NotEmpty
	private String univType;
	@NotEmpty
	private String univName;
	private String univFac;
	private String univDep;
	private String gradLoc;
	private String gradType;
	private String gradName;
	private String gradSchool;
	private String gradDiv;
	private String graduatedIn;
	private String degree;
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
