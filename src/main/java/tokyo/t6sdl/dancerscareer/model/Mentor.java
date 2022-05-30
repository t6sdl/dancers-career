package tokyo.t6sdl.dancerscareer.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tokyo.t6sdl.dancerscareer.model.form.AdditionalMentorInfoForm;
import tokyo.t6sdl.dancerscareer.model.form.MentorForm;

@Data
public class Mentor {
	private int id;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String familyNameJa;
	private String givenNameJa;
	private String familyNameEn;
	private String givenNameEn;
	private String worksAt;
	private String graduatedFrom;
	private String danceClub;
	private byte[] binaryImage;
	private String base64Image;
	private List<AdditionalMentorInfo> additionalMentorInfos = new ArrayList<AdditionalMentorInfo>();

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public class AdditionalMentorInfo {
		private int id;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private String title;
		private String content;

		public void buildFromForm(AdditionalMentorInfoForm form) {
			this.setTitle(form.getTitle());
			this.setContent(form.getContent());
		}
	}

	public void buildFromForm(MentorForm form) {
		this.setFamilyNameJa(form.getFamilyNameJa());
		this.setGivenNameJa(form.getGivenNameJa());
		this.setFamilyNameEn(form.getFamilyNameEn());
		this.setGivenNameEn(form.getGivenNameEn());
		this.setWorksAt(form.getWorksAt());
		this.setGraduatedFrom(form.getGraduatedFrom());
		this.setDanceClub(form.getDanceClub());
		try {
			this.setImageFromBinary(form.getImage().getBytes());
		} catch (IOException e) {
			this.setBinaryImage(null);
			this.setBase64Image(null);
			e.printStackTrace();
		}
		form.getAdditionalMentorInfos().stream().filter(infoForm -> infoForm.getTitle().length() > 0).forEach(infoForm -> {
			AdditionalMentorInfo info = new AdditionalMentorInfo();
			info.buildFromForm(infoForm);
			this.additionalMentorInfos.add(info);
		});
	}

	public void appendAdditionalMentorInfo(int id, LocalDateTime createdAt, LocalDateTime updatedAt, String title, String content) {
		AdditionalMentorInfo info = new AdditionalMentorInfo(id, createdAt, updatedAt, title, content);
		this.additionalMentorInfos.add(info);
	}

	public void setImageFromBinary(byte[] binary) {
		this.binaryImage = binary;
		this.base64Image = base64FromBinary(this.binaryImage);
	}

	public void setImageFromBase64String(String base64) {
		this.base64Image = base64;
		this.binaryImage = binaryFromBase64(base64);
	}

	private String base64FromBinary(byte[] binary) {
		return Base64.getEncoder().encodeToString(binary);
	}

	private byte[] binaryFromBase64(String base64) {
		return Base64.getDecoder().decode(base64);
	}
}
