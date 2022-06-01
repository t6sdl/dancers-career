package tokyo.t6sdl.dancerscareer.model;

import java.util.List;

import lombok.Data;

@Data
public class Experience {
	private int id;
	private int pageView;
	private int likes;
	private String familyName;
	private String givenName;
	private String kanaFamilyName;
	private String kanaGivenName;
	private String sex;
	private String major;
	private String univLoc;
	private String univName;
	private String univFac;
	private String univDep;
	private String gradLoc;
	private String gradName;
	private String gradSchool;
	private String gradDiv;
	private String graduatedIn;
	private String degree;
	private List<String> position;
	private List<String> club;
	private List<String> offer;
	private List<Es> es;
	private List<Interview> interview;
	private boolean isLiked;
}
