package tokyo.t6sdl.dancerscareer2019.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProfileForm {
	@NotEmpty
	private String last_name;
	@NotEmpty
	private String first_name;
	@NotEmpty
	@Pattern(message="カタカナで入力してください", regexp="^[\u30A1-\u30F6ー]+$")
	private String kana_last_name;
	@NotEmpty
	@Pattern(message="カタカナで入力してください", regexp="^[\u30A1-\u30F6ー]+$")
	private String kana_first_name;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String birth_year;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String birth_month;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String birth_day;
	@NotEmpty
	private String sex;
	@NotEmpty
	@Size(min=10)
	@Pattern(regexp="^[0-9]+$")
	private String phone_number;
	@NotEmpty
	private String major;
	@NotEmpty
	@Pattern(regexp="^(?!default)$")
	private String prefecture;
	@NotEmpty
	@Pattern(regexp="^(?!default)$")
	private String university;
	@NotEmpty
	@Pattern(regexp="^(?!default)$")
	private String faculty;
	@NotEmpty
	@Pattern(regexp="^(?!default)$")
	private String department;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String graduation_year;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String graduation_month;
	@NotEmpty
	private String academic_degree;
	@NotEmpty
	private List<String> position;
}
