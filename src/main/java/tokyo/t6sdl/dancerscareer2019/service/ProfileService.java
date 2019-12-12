package tokyo.t6sdl.dancerscareer2019.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.Student;
import tokyo.t6sdl.dancerscareer2019.model.form.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.repository.ProfileRepository;

@RequiredArgsConstructor
@Service
public class ProfileService {
	private final ProfileRepository profileRepository;
	
	public void register(Profile newProfile, String loggedInEmail) {
		newProfile.setEmail(loggedInEmail);
		newProfile.convertForData();
		profileRepository.insert(newProfile);
	}
	
	public void delete(String email) {
		profileRepository.delete(email);
	}
	
	public void update(Profile profile, String loggedInEmail) {
		profile.setEmail(loggedInEmail);
		profile.convertForData();
		profileRepository.update(profile);
	}
	
	public void updateLikes(String email, List<String> likes) {
		profileRepository.updateLikes(email, likes);
	}
	
	public Profile getProfileByEmail(String email) {
		return profileRepository.findOneByEmail(email);
	}
	
	public Map<String, Object> getProfiles(int sort) {
		return profileRepository.find(sort);
	}
	
	public Map<String, Object> getProfilesByName(int sort, String kanaFamilyName, String kanaGivenName) {
		return profileRepository.findByName(sort, kanaFamilyName, kanaGivenName);
	}
	
	public Map<String, Object> getProfilesByFamilyName(int sort, String kanaFamilyName) {
		return profileRepository.findByFamilyName(sort, kanaFamilyName);
	}
	
	public Map<String, Object> getProfilesByUnivLoc(int sort, String univLoc) {
		return profileRepository.findByUnivLoc(sort, univLoc);
	}
	
	public Map<String, Object> getProfilesByUnivName(int sort, String univLoc, String univName) {
		return profileRepository.findByUnivName(sort, univLoc, univName);
	}
	
	public Map<String, Object> getProfilesByUnivFac(int sort, String univLoc, String univName, String univFac) {
		return profileRepository.findByUnivFac(sort, univLoc, univName, univFac);
	}
	
	public Map<String, Object> getProfilesByUnivDep(int sort, String univLoc, String univName, String univFac, String univDep) {
		return profileRepository.findByUnivDep(sort, univLoc, univName, univFac, univDep);
	}
	
	public Map<String, Object> getProfilesByPosition(int sort, List<String> position, boolean andSearch) {
		return profileRepository.findByPosition(sort, position, andSearch);
	}
	
	public String getFamilyNameByEmail(String email) {
		return profileRepository.findFamilyNameByEmail(email);
	}
	
	public List<String> getLikesByEmail(String email) {
		return profileRepository.findLikesByEmail(email);
	}
	
	public Profile convertProfileFormIntoProfile(ProfileForm form) {
		Profile profile = new Profile();
		if (form.getGraduationMonth().length() == 1) {
			String gm = form.getGraduationMonth();
			form.setGraduationMonth("0" + gm);
		}
		String graduatedIn = form.getGraduationYear() + "/" + form.getGraduationMonth();
		profile.setFamilyName(form.getFamilyName());
		profile.setGivenName(form.getGivenName());
		profile.setKanaFamilyName(form.getKanaFamilyName());
		profile.setKanaGivenName(form.getKanaGivenName());
		profile.setBirth(LocalDate.of(Integer.parseInt(form.getBirthYear()), Integer.parseInt(form.getBirthMonth()), Integer.parseInt(form.getBirthDay())));
		profile.setSex(form.getSex());
		profile.setPhone(form.getPhone());
		profile.setMajor(form.getMajor());
		profile.setUnivLoc(form.getUnivLoc());
		profile.setUnivName(form.getUnivName());
		profile.setUnivFac(form.getUnivFac());
		profile.setUnivDep(form.getUnivDep());
		profile.setGradLoc(form.getGradLoc());
		profile.setGradName(form.getGradName());
		profile.setGradSchool(form.getGradSchool());
		profile.setGradDiv(form.getGradDiv());
		profile.setGraduatedIn(graduatedIn);
		profile.setDegree(form.getDegree());
		profile.setClub(form.getClub());
		profile.setPosition(form.getPosition());
		return profile;
	}
	
	public ProfileForm convertProfileIntoProfileForm(Profile profile) {
		if (Objects.equals(profile, null)) {
			ProfileForm form = new ProfileForm();
			form.setUnivName("");
			form.setUnivFac("");
			form.setUnivDep("");
			return form;
		}
		ProfileForm form = new ProfileForm();
		String[] split = profile.getGraduatedIn().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		form.setFamilyName(profile.getFamilyName());
		form.setGivenName(profile.getGivenName());
		form.setKanaFamilyName(profile.getKanaFamilyName());
		form.setKanaGivenName(profile.getKanaGivenName());
		form.setBirthYear(String.valueOf(profile.getBirth().getYear()));
		form.setBirthMonth(String.valueOf(profile.getBirth().getMonthValue()));
		form.setBirthDay(String.valueOf(profile.getBirth().getDayOfMonth()));
		form.setSex(profile.getSex());
		form.setPhone(profile.getPhone());
		form.setMajor(profile.getMajor());
		form.setUnivLoc(profile.getUnivLoc());
		form.setUnivName(profile.getUnivName());
		form.setUnivFac(profile.getUnivFac());
		form.setUnivDep(profile.getUnivDep());
		form.setGradLoc(profile.getGradLoc());
		form.setGradName(profile.getGradName());
		form.setGradSchool(profile.getGradSchool());
		form.setGradDiv(profile.getGradDiv());
		form.setGraduationYear(split[0]);
		form.setGraduationMonth(split[1]);
		form.setDegree(profile.getDegree());
		form.setClub(profile.getClub());
		form.setPosition(profile.getPosition());
		return form;
	}
	
	public Student convertProfileIntoStudent(Profile profile) {
		if (Objects.equals(profile, null)) {
			return new Student();
		}
		Student student = new Student();
		student.setEmail(profile.getEmail());
		student.setFamilyName(profile.getFamilyName());
		student.setGivenName(profile.getGivenName());
		student.setKanaFamilyName(profile.getKanaFamilyName());
		student.setKanaGivenName(profile.getKanaGivenName());
		student.setBirth(profile.getBirth());
		student.setSex(profile.getSex());
		student.setPhone(profile.getPhone());
		student.setMajor(profile.getMajor());
		student.setUnivLoc(profile.getUnivLoc());
		student.setUnivName(profile.getUnivName());
		student.setUnivFac(profile.getUnivFac());
		student.setUnivDep(profile.getUnivDep());
		student.setGradLoc(profile.getGradLoc());
		student.setGradName(profile.getGradName());
		student.setGradSchool(profile.getGradSchool());
		student.setGradDiv(profile.getGradDiv());
		student.setGraduatedIn(profile.getGraduatedIn());
		student.setDegree(profile.getDegree());
		student.setClub(profile.getClub());
		student.setPosition(profile.getPosition());
		student.setLikes(profile.getLikes());
		return student;
	}
	
	public boolean isCompleteProfile(Profile profile) {
		if (Objects.equals(profile, null)) {
			return false;
		}
		profile.convertForDisplay();
		for (Field field: profile.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if (field.getType().equals(String.class) && Objects.equals(field.get(profile), null)) {
					return false;
				}
			} catch (IllegalAccessException e) {
				return false;
			}
		}
		return true;
	}
}
