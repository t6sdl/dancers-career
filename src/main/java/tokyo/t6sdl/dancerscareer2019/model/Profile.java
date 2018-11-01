package tokyo.t6sdl.dancerscareer2019.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class Profile {
	public static final List<String> POSITION_LIST = Arrays.asList("代表", "副代表", "会計", "広報", "渉外・営業", "ジャンルリーダー", "振付師", "公演舞台監督", "公演総合演出", "公演ストーリー制作", "音響制作", "照明制作", "映像制作", "衣装制作", "イベントオーガナイザー", "新歓係", "合宿統括", "役職なし", "その他");
	
	private String email;
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
	private List<String> position;
	private List<String> likes;
	
	public void convertForDisplay() {
		StringBuilder graduation = new StringBuilder();
		String date_of_birth = this.getDate_of_birth().format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
		String[] split = this.getGraduation().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		this.setGraduation(graduation.append(split[0]).append("年").append(split[1]).append("月").toString());
		this.setDate_of_birth_for_display(date_of_birth);
		if (this.getFaculty().equals("-")) {
			this.setFaculty("");
		}
		if (this.getDepartment().equals("-")) {
			this.setDepartment("");
		}
		if (this.getGrad_school_of().equals("-")) {
			this.setGrad_school_of("");
		}
		if (this.getProgram_in().equals("-")) {
			this.setProgram_in("");
		}
	}
}