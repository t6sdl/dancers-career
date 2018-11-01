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
		profileRepository.updateAny(profile);
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
	
	public Map<String, Object> getProfilesByName(int sort, String kanaLastName, String kanaFirstName) {
		return profileRepository.findByName(sort, kanaLastName, kanaFirstName);
	}
	
	public Map<String, Object> getProfilesByLastName(int sort, String kanaLastName) {
		return profileRepository.findByLastName(sort, kanaLastName);
	}
	
	public Map<String, Object> getProfilesByPrefecture(int sort, String prefecture) {
		return profileRepository.findByPrefecture(sort, prefecture);
	}
	
	public Map<String, Object> getProfilesByUniversity(int sort, String prefecture, String university) {
		return profileRepository.findByUniversity(sort, prefecture, university);
	}
	
	public Map<String, Object> getProfilesByFaculty(int sort, String prefecture, String university, String faculty) {
		return profileRepository.findByFaculty(sort, prefecture, university, faculty);
	}
	
	public Map<String, Object> getProfilesByDepartment(int sort, String prefecture, String university, String faculty, String department) {
		return profileRepository.findByDepartment(sort, prefecture, university, faculty, department);
	}
	
	public Map<String, Object> getProfilesByPosition(int sort, List<String> position, boolean andSearch) {
		return profileRepository.findByPosition(sort, position, andSearch);
	}
	
	public String getLastNameByEmail(String email) {
		return profileRepository.findLastNameByEmail(email);
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
		String graduation = form.getGraduationYear() + "/" + form.getGraduationMonth();
		profile.setLast_name(form.getLastName());
		profile.setFirst_name(form.getFirstName());
		profile.setKana_last_name(form.getKanaLastName());
		profile.setKana_first_name(form.getKanaFirstName());
		profile.setDate_of_birth(LocalDate.of(Integer.parseInt(form.getBirthYear()), Integer.parseInt(form.getBirthMonth()), Integer.parseInt(form.getBirthDay())));
		profile.setSex(form.getSex());
		profile.setPhone_number(form.getPhoneNumber());
		profile.setMajor(form.getMajor());
		profile.setUniv_pref(form.getUnivPref());
		profile.setUniv_name(form.getUnivName());
		profile.setFaculty(form.getFaculty());
		profile.setDepartment(form.getDepartment());
		profile.setGrad_school_pref(form.getGradSchoolPref());
		profile.setGrad_school_name(form.getGradSchoolName());
		profile.setGrad_school_of(form.getGradSchoolOf());
		profile.setProgram_in(form.getProgramIn());
		profile.setGraduation(graduation);
		profile.setAcademic_degree(form.getAcademicDegree());
		profile.setPosition(form.getPosition());
		return profile;
	}
	
	public ProfileForm convertProfileIntoProfileForm(Profile profile) {
		if (Objects.equals(profile, null)) {
			ProfileForm form = new ProfileForm();
			form.setUnivName("");
			form.setFaculty("");
			form.setDepartment("");
			return form;
		}
		ProfileForm form = new ProfileForm();
		String[] split = profile.getGraduation().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		form.setLastName(profile.getLast_name());
		form.setFirstName(profile.getFirst_name());
		form.setKanaLastName(profile.getKana_last_name());
		form.setKanaFirstName(profile.getKana_first_name());
		form.setBirthYear(String.valueOf(profile.getDate_of_birth().getYear()));
		form.setBirthMonth(String.valueOf(profile.getDate_of_birth().getMonthValue()));
		form.setBirthDay(String.valueOf(profile.getDate_of_birth().getDayOfMonth()));
		form.setSex(profile.getSex());
		form.setPhoneNumber(profile.getPhone_number());
		form.setMajor(profile.getMajor());
		form.setUnivPref(profile.getUniv_pref());
		form.setUnivName(profile.getUniv_name());
		form.setFaculty(profile.getFaculty());
		form.setDepartment(profile.getDepartment());
		form.setGradSchoolPref(profile.getGrad_school_pref());
		form.setGradSchoolName(profile.getGrad_school_name());
		form.setGradSchoolOf(profile.getGrad_school_of());
		form.setProgramIn(profile.getProgram_in());
		form.setGraduationYear(split[0]);
		form.setGraduationMonth(split[1]);
		form.setAcademicDegree(profile.getAcademic_degree());
		form.setPosition(profile.getPosition());
		return form;
	}
	
	public Student convertProfileIntoStudent(Profile profile) {
		if (Objects.equals(profile, null)) {
			return new Student();
		}
		Student student = new Student();
		student.setEmail(profile.getEmail());
		student.setLast_name(profile.getLast_name());
		student.setFirst_name(profile.getFirst_name());
		student.setKana_last_name(profile.getKana_last_name());
		student.setKana_first_name(profile.getKana_first_name());
		student.setDate_of_birth(profile.getDate_of_birth());
		student.setSex(profile.getSex());
		student.setPhone_number(profile.getPhone_number());
		student.setMajor(profile.getMajor());
		student.setUniv_pref(profile.getUniv_pref());
		student.setUniv_name(profile.getUniv_name());
		student.setFaculty(profile.getFaculty());
		student.setDepartment(profile.getDepartment());
		student.setGrad_school_pref(profile.getGrad_school_pref());
		student.setGrad_school_name(profile.getGrad_school_name());
		student.setGrad_school_of(profile.getGrad_school_of());
		student.setProgram_in(profile.getProgram_in());
		student.setGraduation(profile.getGraduation());
		student.setAcademic_degree(profile.getAcademic_degree());
		student.setPosition(profile.getPosition());
		student.setLikes(profile.getLikes());
		return student;
	}
	
	public boolean isCompleteProfile(Profile profile) {
		if (Objects.equals(profile, null)) {
			return false;
		}
		ProfileForm form = this.convertProfileIntoProfileForm(profile);
		for (Field field: form.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				if (field.get(form) == null) {
					return false;
				}
			} catch (IllegalAccessException e) {
				return false;
			}
		}
		return true;
	}
}
