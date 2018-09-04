package tokyo.t6sdl.dancerscareer2019.model;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class Profile {
	private String email;
	private String last_name;
	private String first_name;
	private String kana_last_name;
	private String kana_first_name;
	private String date_of_birth;
	private LocalDate date_of_birth_for_calc;
	private String sex;
	private String phone_number;
	private String major;
	private String prefecture;
	private String university;
	private String faculty;
	private String department;
	private String graduation;
	private String academic_degree;
	private List<String> position;
	
	public void convertForDisplay() {
		StringBuffer graduation = new StringBuffer();
		StringBuffer date_of_birth = new StringBuffer();
		String[] split = this.getGraduation().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		this.setGraduation(graduation.append(split[0]).append("年").append(split[1]).append("月").toString());
		date_of_birth.append(this.getDate_of_birth_for_calc().getYear()).append("年").append(this.getDate_of_birth_for_calc().getMonthValue()).append("月").append(this.getDate_of_birth_for_calc().getDayOfMonth()).append("日");
		this.setDate_of_birth(date_of_birth.toString());
	}
}