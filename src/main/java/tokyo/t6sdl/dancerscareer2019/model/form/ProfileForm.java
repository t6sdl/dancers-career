package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProfileForm {
	@NotEmpty
	private String lastName;
	@NotEmpty
	private String firstName;
	@NotEmpty
	@Pattern(message="カタカナで入力してください", regexp="^[\u30A1-\u30F6ー]+$")
	private String kanaLastName;
	@NotEmpty
	@Pattern(message="カタカナで入力してください", regexp="^[\u30A1-\u30F6ー]+$")
	private String kanaFirstName;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String birthYear;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String birthMonth;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String birthDay;
	@NotEmpty
	private String sex;
	@NotEmpty
	@Size(min=10)
	@Pattern(regexp="^[0-9]+$")
	private String phoneNumber;
	@NotEmpty
	private String major;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String univPref;
	@Pattern(regexp="^(?!default).*$")
	private String univCategory;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String univName;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String faculty;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String department;
	private String gradSchoolPref;
	private String gradSchoolCategory;
	private String gradSchoolName;
	private String gradSchoolOf;
	private String programIn;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String graduationYear;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String graduationMonth;
	@NotEmpty
	private String academicDegree;
	@NotEmpty
	private List<String> position;
	private boolean applyLineNotify;
}
