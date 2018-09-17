package tokyo.t6sdl.dancerscareer2019.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Student {
	private String email;
	private boolean valid_email;
	private String last_login_for_display;
	private LocalDateTime last_login;
	private String last_name;
	private String first_name;
	private String kana_last_name;
	private String kana_first_name;
	private String date_of_birth_for_display;
	private LocalDate date_of_birth;
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
		StringBuffer last_login = new StringBuffer();
		StringBuffer graduation = new StringBuffer();
		StringBuffer date_of_birth = new StringBuffer();
		String[] split = this.getGraduation().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		this.setLast_login_for_display(last_login.append(this.getLast_login().getYear()).append("年").append(this.getLast_login().getMonthValue()).append("月").append(this.getLast_login().getDayOfMonth()).append("日 ").append(this.getLast_login().getHour()).append("時").append(this.getLast_login().getMinute()).append("分").toString());
		this.setGraduation(graduation.append(split[0]).append("年").append(split[1]).append("月").toString());
		this.setDate_of_birth_for_display(date_of_birth.append(this.getDate_of_birth().getYear()).append("年").append(this.getDate_of_birth().getMonthValue()).append("月").append(this.getDate_of_birth().getDayOfMonth()).append("日").toString());
	}
}