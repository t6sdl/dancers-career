package tokyo.t6sdl.dancerscareer2019.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class ProfileForm {
	@NotEmpty
	private String last_name;
	@NotEmpty
	private String first_name;
	@NotEmpty
	private String kana_last_name;
	@NotEmpty
	private String kana_first_name;
	private String birth_year;
	private String birth_month;
	private String birth_day;
	private String sex;
	private String phone_number;
	private String major;
	private String university;
	private String faculty;
	private String department;
	private String graduation_year;
	private String graduation_month;
	private String academic_degree;
	private List<String> position;
	private String email;
	private String password;
}
