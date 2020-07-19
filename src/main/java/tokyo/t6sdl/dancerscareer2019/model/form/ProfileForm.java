package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ProfileForm {
	@NotEmpty
	private String familyName;
	@NotEmpty
	private String givenName;
	@NotEmpty
	@Pattern(message="カタカナで入力してください", regexp="^[\u30A1-\u30F6ー]+$")
	private String kanaFamilyName;
	@NotEmpty
	@Pattern(message="カタカナで入力してください", regexp="^[\u30A1-\u30F6ー]+$")
	private String kanaGivenName;
	@NotEmpty
	@Pattern(message="選択してください", regexp="^[0-9]+$")
	private String birthYear;
	@NotEmpty
	@Pattern(message="選択してください", regexp="^[0-9]+$")
	private String birthMonth;
	@NotEmpty
	@Pattern(message="選択してください", regexp="^[0-9]+$")
	private String birthDay;
	@NotEmpty
	private String sex;
	@NotEmpty
	@Size(min=10)
	@Pattern(regexp="^[0-9]+$")
	private String phone;
	@NotEmpty
	private String major;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String univLoc;
	@Pattern(regexp="^(?!default).*$")
	private String univType;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String univName;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String univFac;
	@NotEmpty
	@Pattern(regexp="^(?!default).*$")
	private String univDep;
	private String gradLoc;
	private String gradType;
	private String gradName;
	private String gradSchool;
	private String gradDiv;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String graduationYear;
	@NotEmpty
	@Pattern(regexp="^[0-9]+$")
	private String graduationMonth;
	@NotEmpty
	private String degree;
	@NotEmpty
	private String club;
	@NotEmpty
	private List<String> position;
	private boolean applyLineNotify;
}
