package tokyo.t6sdl.dancerscareer2019.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	private String club;
	private List<String> position;
	private List<String> likes;
	
	public void convertForDisplay() {
		String last_login = this.getLast_login().format(DateTimeFormatter.ofPattern("yyyy年M月d日 H時m分s秒"));
		String date_of_birth = this.getDate_of_birth().format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
		StringBuilder graduation = new StringBuilder();
		String[] split = this.getGraduation().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		this.setGraduation(graduation.append(split[0]).append("年").append(split[1]).append("月").toString());
		this.setLast_login_for_display(last_login);
		this.setDate_of_birth_for_display(date_of_birth);
	}
}