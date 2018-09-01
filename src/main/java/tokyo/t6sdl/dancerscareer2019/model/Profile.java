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
	private LocalDate date_of_birth;
	private String sex;
	private String phone_number;
	private String major;
	private String university;
	private String faculty;
	private String department;
	private String graduation;
	private String academic_degree;
	private List<String> position;
}