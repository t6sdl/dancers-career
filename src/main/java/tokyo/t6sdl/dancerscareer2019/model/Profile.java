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
		StringBuilder graduatedIn = new StringBuilder();
		String birth = this.getBirth().format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
		String[] split = this.getGraduatedIn().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		this.setGraduatedIn(graduatedIn.append(split[0]).append("年").append(split[1]).append("月").toString());
		this.setBirthForDisplay(birth);
		if (this.getUnivFac().equals("-")) {
			this.setUnivFac("");
		}
		if (this.getUnivDep().equals("-")) {
			this.setUnivDep("");
		}
		if (this.getGradSchool().equals("-")) {
			this.setGradSchool("");
		}
		if (this.getGradDiv().equals("-")) {
			this.setGradDiv("");
		}
	}
	
	public void convertForData() {
		if (this.getDegree().equals("学部卒")) {
			this.setGradLoc("");
			this.setGradName("");
			this.setGradSchool("");
			this.setGradDiv("");
		}
	}
}