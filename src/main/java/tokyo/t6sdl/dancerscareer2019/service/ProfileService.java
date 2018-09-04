package tokyo.t6sdl.dancerscareer2019.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.repository.ProfileRepository;

@Service
public class ProfileService {
	private final ProfileRepository profileRepository;
	
	public ProfileService(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}
	
	public void register(Profile newProfile, String loggedInEmail) {
		newProfile.setEmail(loggedInEmail);
		profileRepository.insert(newProfile);
	}
	
	public void delete(String email) {
		profileRepository.delete(email);
	}
	
	public void update(Profile profile, String loggedInEmail) {
		profile.setEmail(loggedInEmail);
		profileRepository.updateAny(profile);
	}
	
	public Profile getProfileByEmail(String email) {
		return profileRepository.findOneByEmail(email);
	}
	
	public Profile convertProfileFormIntoProfile(ProfileForm form) {
		Profile profile = new Profile();
		if (form.getGraduation_month().length() == 1) {
			String gm = form.getGraduation_month();
			form.setGraduation_month("0" + gm);
		}
		String graduation = form.getGraduation_year() + "/" + form.getGraduation_month();
		profile.setLast_name(form.getLast_name());
		profile.setFirst_name(form.getFirst_name());
		profile.setKana_last_name(form.getKana_last_name());
		profile.setKana_first_name(form.getKana_first_name());
		profile.setDate_of_birth_for_calc(LocalDate.of(Integer.parseInt(form.getBirth_year()), Integer.parseInt(form.getBirth_month()), Integer.parseInt(form.getBirth_day())));
		profile.setSex(form.getSex());
		profile.setPhone_number(form.getPhone_number());
		profile.setMajor(form.getMajor());
		profile.setPrefecture(form.getPrefecture());
		profile.setUniversity(form.getUniversity());
		profile.setFaculty(form.getFaculty());
		profile.setDepartment(form.getDepartment());
		profile.setGraduation(graduation);
		profile.setAcademic_degree(form.getAcademic_degree());
		profile.setPosition(form.getPosition());
		return profile;
	}
	
	public ProfileForm convertProfileIntoProfileForm(Profile profile) {
		ProfileForm form = new ProfileForm();
		String[] split = profile.getGraduation().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		form.setLast_name(profile.getLast_name());
		form.setFirst_name(profile.getFirst_name());
		form.setKana_last_name(profile.getKana_last_name());
		form.setKana_first_name(profile.getKana_first_name());
		form.setBirth_year(String.valueOf(profile.getDate_of_birth_for_calc().getYear()));
		form.setBirth_month(String.valueOf(profile.getDate_of_birth_for_calc().getMonthValue()));
		form.setBirth_day(String.valueOf(profile.getDate_of_birth_for_calc().getDayOfMonth()));
		form.setSex(profile.getSex());
		form.setPhone_number(profile.getPhone_number());
		form.setMajor(profile.getMajor());
		form.setPrefecture(profile.getPrefecture());
		form.setUniversity(profile.getUniversity());
		form.setFaculty(profile.getFaculty());
		form.setDepartment(profile.getDepartment());
		form.setGraduation_year(split[0]);
		form.setGraduation_month(split[1]);
		form.setAcademic_degree(profile.getAcademic_degree());
		form.setPosition(profile.getPosition());
		return form;
	}
}
