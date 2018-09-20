package tokyo.t6sdl.dancerscareer2019.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;
import tokyo.t6sdl.dancerscareer2019.model.form.EsForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ExperienceForm;
import tokyo.t6sdl.dancerscareer2019.model.form.InterviewForm;
import tokyo.t6sdl.dancerscareer2019.repository.ExperienceRepository;

@RequiredArgsConstructor
@Service
public class ExperienceService {
	private final ExperienceRepository experienceRepository;
	
	public List<Experience> getExperiences() {
		return experienceRepository.find();
	}
	
	public List<Experience> getExperiencesByName(String kanaLastName, String kanaFirstName) {
		return experienceRepository.findByName(kanaLastName, kanaFirstName);
	}
	
	public List<Experience> getExperiencesByLastName(String kanaLastName) {
		return experienceRepository.findByLastName(kanaLastName);
	}
	
	public List<Experience> getExperiencesByPrefecture(String prefecture) {
		return experienceRepository.findByPrefecture(prefecture);
	}
	
	public List<Experience> getExperiencesByUniversity(String prefecture, String university) {
		return experienceRepository.findByUniversity(prefecture, university);
	}
	
	public List<Experience> getExperiencesByFaculty(String prefecture, String university, String faculty) {
		return experienceRepository.findByFaculty(prefecture, university, faculty);
	}
	
	public List<Experience> getExperiencesByDepartment(String prefecture, String university, String faculty, String department) {
		return experienceRepository.findByDepartment(prefecture, university, faculty, department);
	}
	
	public List<Experience> getExperiencesByPosition(List<String> position, String method) {
		return experienceRepository.findByPosition(position, method);
	}
	
	public void register(Experience newExperience) {
		experienceRepository.insert(newExperience);
	}
	
	public void delete(int experienceId) {
		experienceRepository.delete(experienceId);
	}
	
	public void update(Experience experience) {
		experienceRepository.update(experience);
	}
	
	public void registerEs(Es newEs) {
		experienceRepository.insertEs(newEs);
	}
	
	public void deleteEs(int experienceId, int esId) {
		experienceRepository.deleteEs(experienceId, esId);
	}
	
	public void updateEs(Es es) {
		experienceRepository.updateEs(es);
	}
	
	public void registerInterview(Interview newInterview) {
		experienceRepository.insertInterview(newInterview);
	}
	
	public void deleteInterview(int experienceId, int interviewId) {
		experienceRepository.deleteInterview(experienceId, interviewId);
	}
	
	public void updateInterview(Interview interview) {
		experienceRepository.updateInterview(interview);
	}
	
	public Experience convertExperienceFormIntoExperience(ExperienceForm form) {
		Experience experience = new Experience();
		experience.setLast_name(form.getLastName());
		experience.setFirst_name(form.getFirstName());
		experience.setKana_last_name(form.getKanaLastName());
		experience.setKana_first_name(form.getKanaFirstName());
		experience.setSex(form.getSex());
		experience.setMajor(form.getMajor());
		experience.setPrefecture(form.getPrefecture());
		experience.setUniversity(form.getUniversity());
		experience.setFaculty(form.getFaculty());
		experience.setDepartment(form.getDepartment());
		experience.setGraduation(form.getGraduation());
		experience.setAcademic_degree(form.getAcademicDegree());
		experience.setPosition(form.getPosition());
		experience.setClub(form.getClub());
		experience.setOffer(form.getOffer());
		experience.setEs(this.convertEsFormIntoEs(form.getEs()));
		experience.setInterview(this.convertInterviewFormIntoInterview(form.getInterview()));
		return experience;
	}
	
	public List<Es> convertEsFormIntoEs(List<EsForm> form) {
		List<Es> es = new ArrayList<Es>();
		form.forEach(formItem -> {;
			Es esItem = new Es();
			esItem.setCorp(formItem.getCorp());
			esItem.setResult(formItem.getResult());
			esItem.setQuestion(formItem.getQuestion());
			esItem.setAnswer(formItem.getAnswer());
			esItem.setAdvice(formItem.getAdvice());
			es.add(esItem);
		});
		return es;
	}
	
	public List<Interview> convertInterviewFormIntoInterview(List<InterviewForm> form) {
		List<Interview> interview = new ArrayList<Interview>();
		form.forEach(formItem -> {;
			Interview interviewItem = new Interview();
			interviewItem.setQuestion(formItem.getQuestion());
			interviewItem.setAnswer(formItem.getAnswer());
			interview.add(interviewItem);
		});
		return interview;
	}
}
