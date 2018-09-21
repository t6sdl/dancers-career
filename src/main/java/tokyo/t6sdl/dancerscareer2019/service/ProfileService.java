package tokyo.t6sdl.dancerscareer2019.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

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
		profileRepository.insert(newProfile);
	}
	
	public void delete(String email) {
		profileRepository.delete(email);
	}
	
	public void update(Profile profile, String loggedInEmail) {
		profile.setEmail(loggedInEmail);
		profileRepository.updateAny(profile);
	}
	
	public List<Profile> getProfiles() {
		return profileRepository.find();
	}
	
	public Profile getProfileByEmail(String email) {
		if (profileRepository.findOneByEmail(email) == null) {
			return new Profile();
		}
		return profileRepository.findOneByEmail(email);
	}
	
	public List<Profile> getProfilesByName(String kanaLastName, String kanaFirstName) {
		return profileRepository.findByName(kanaLastName, kanaFirstName);
	}
	
	public List<Profile> getProfilesByLastName(String kanaLastName) {
		return profileRepository.findByLastName(kanaLastName);
	}
	
	public List<Profile> getProfilesByPrefecture(String prefecture) {
		return profileRepository.findByPrefecture(prefecture);
	}
	
	public List<Profile> getProfilesByUniversity(String prefecture, String university) {
		return profileRepository.findByUniversity(prefecture, university);
	}
	
	public List<Profile> getProfilesByFaculty(String prefecture, String university, String faculty) {
		return profileRepository.findByFaculty(prefecture, university, faculty);
	}
	
	public List<Profile> getProfilesByDepartment(String prefecture, String university, String faculty, String department) {
		return profileRepository.findByDepartment(prefecture, university, faculty, department);
	}
	
	public List<Profile> getProfilesByPosition(List<String> position, String method) {
		if (!(method.equals("AND")) && !(method.equals("OR"))) {
			return null;
		}
		return profileRepository.findByPosition(position, method);
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
		profile.setPrefecture(form.getPrefecture());
		profile.setUniversity(form.getUniversity());
		profile.setFaculty(form.getFaculty());
		profile.setDepartment(form.getDepartment());
		profile.setGraduation(graduation);
		profile.setAcademic_degree(form.getAcademicDegree());
		profile.setPosition(form.getPosition());
		return profile;
	}
	
	public ProfileForm convertProfileIntoProfileForm(Profile profile) {
		if (profile.getEmail() == null) {
			ProfileForm form = new ProfileForm();
			form.setUniversity("");
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
		form.setPrefecture(profile.getPrefecture());
		form.setUniversity(profile.getUniversity());
		form.setFaculty(profile.getFaculty());
		form.setDepartment(profile.getDepartment());
		form.setGraduationYear(split[0]);
		form.setGraduationMonth(split[1]);
		form.setAcademicDegree(profile.getAcademic_degree());
		form.setPosition(profile.getPosition());
		return form;
	}
	
	public Student convertProfileIntoStudent(Profile profile) {
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
		student.setPrefecture(profile.getPrefecture());
		student.setUniversity(profile.getUniversity());
		student.setFaculty(profile.getFaculty());
		student.setDepartment(profile.getDepartment());
		student.setGraduation(profile.getGraduation());
		student.setAcademic_degree(profile.getAcademic_degree());
		student.setPosition(profile.getPosition());
		return student;
	}
	
	public boolean isCompleteProfile(Profile profile) {
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
