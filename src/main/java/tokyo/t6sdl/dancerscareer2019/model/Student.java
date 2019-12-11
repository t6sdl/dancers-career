package tokyo.t6sdl.dancerscareer2019.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Data;

@Data
public class Student {
	private String email;
	private boolean validEmail;
	private String lastLoginForDisplay;
	private LocalDateTime lastLogin;
	private String familyName;
	private String givenName;
	private String kanaFamilyName;
	private String kanaGivenName;
	private String birthForDisplay;
	private LocalDate birth;
	private String sex;
	private String phone;
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
	private String club;
	private List<String> position;
	private List<String> likes;
	
	public void convertForDisplay() {
		String last_login = this.getLastLogin().format(DateTimeFormatter.ofPattern("yyyy/M/d H:m"));
		String date_of_birth = this.getBirth().format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
		StringBuilder graduation = new StringBuilder();
		String[] split = this.getGraduatedIn().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		this.setGraduatedIn(graduation.append(split[0]).append("年").append(split[1]).append("月").toString());
		this.setLastLoginForDisplay(last_login);
		this.setBirthForDisplay(date_of_birth);
	}
}