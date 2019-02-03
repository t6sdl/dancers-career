package tokyo.t6sdl.dancerscareer2019.model;

import java.util.List;

import lombok.Data;

@Data
public class Experience {
	private int experience_id;
	private int page_view;
	private int likes;
	private String last_name;
	private String first_name;
	private String kana_last_name;
	private String kana_first_name;
	private String sex;
	private String major;
	private String univ_pref;
	private String univ_name;
	private String faculty;
	private String department;
	private String grad_school_pref;
	private String grad_school_name;
	private String grad_school_of;
	private String program_in;
	private String graduation;
	private String academic_degree;
	private List<String> position;
	private List<String> club;
	private List<String> offer;
	private List<Es> es;
	private List<Interview> interview;
	private boolean is_liked;
}
