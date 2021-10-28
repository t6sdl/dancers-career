package tokyo.t6sdl.dancerscareer2019.model.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import tokyo.t6sdl.dancerscareer2019.model.form.AdditionalMentorInfoForm;

@Data
public class MentorForm {
	@NotEmpty
	private String familyNameJa;
	@NotEmpty
	private String givenNameJa;
	@NotEmpty
	private String familyNameEn;
	@NotEmpty
	private String givenNameEn;
	@NotEmpty
	private String worksAt;
	@NotEmpty
	private String graduatedFrom;
	@NotEmpty
	private String danceClub;
	@NotNull
	private MultipartFile image;
	private List<AdditionalMentorInfoForm> additionalMentorInfos = new ArrayList<AdditionalMentorInfoForm>();
}
