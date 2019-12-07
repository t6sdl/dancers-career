package tokyo.t6sdl.dancerscareer2019.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
	
	public Experience getExperienceById(int experienceId, boolean all, boolean pvCount) {
		return experienceRepository.findOneById(experienceId, all, pvCount);
	}
	
	public Experience getALittleExperienceById(int experienceId) {
		return experienceRepository.findALittleOneById(experienceId);
	}
	
	public Map<String, Object> getExperiences(int sort) {
		return experienceRepository.find(sort);
	}
	
	public Map<String, Object> getExperiencesByName(int sort, String kanaLastName, String kanaFirstName) {
		return experienceRepository.findByName(sort, kanaLastName, kanaFirstName);
	}
	
	public Map<String, Object> getExperiencesByLastName(int sort, String kanaLastName) {
		return experienceRepository.findByLastName(sort, kanaLastName);
	}
	
	public Map<String, Object> getExperiencesByPrefecture(int sort, String prefecture) {
		return experienceRepository.findByPrefecture(sort, prefecture);
	}
	
	public Map<String, Object> getExperiencesByUniversity(int sort, String prefecture, String university) {
		return experienceRepository.findByUniversity(sort, prefecture, university);
	}
	
	public Map<String, Object> getExperiencesByFaculty(int sort, String prefecture, String university, String faculty) {
		return experienceRepository.findByFaculty(sort, prefecture, university, faculty);
	}
	
	public Map<String, Object> getExperiencesByDepartment(int sort, String prefecture, String university, String faculty, String department) {
		return experienceRepository.findByDepartment(sort, prefecture, university, faculty, department);
	}
	
	public Map<String, Object> getExperiencesByPosition(int sort, List<String> position, boolean andSearch) {
		return experienceRepository.findByPosition(sort, position, andSearch);
	}
	
	public Map<String, Object> getExperiencesByCreatedAt() {
		return experienceRepository.findByCreatedAt();
	}
	
	public Es getEsById(int experienceId, int esId) {
		return experienceRepository.findEsById(experienceId, esId);
	}
	
	public Interview getInterviewById(int experienceId, int interviewId) {
		return experienceRepository.findInterviewById(experienceId, interviewId);
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
	
	public void updateLikes(int experience_id, boolean increment) {
		experienceRepository.updateLikes(experience_id, increment);
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
		experience.setFamilyName(form.getFamilyName());
		experience.setGivenName(form.getGivenName());
		experience.setKanaFamilyName(form.getKanaFamilyName());
		experience.setKanaGivenName(form.getKanaGivenName());
		experience.setSex(form.getSex());
		experience.setMajor(form.getMajor());
		experience.setUnivLoc(form.getUnivLoc());
		experience.setUnivName(form.getUnivName());
		experience.setUnivFac(form.getUnivFac());
		experience.setUnivDep(form.getUnivDep());
		experience.setGradLoc(form.getGradLoc());
		experience.setGradName(form.getGradName());
		experience.setGradSchool(form.getGradSchool());
		experience.setGradDiv(form.getGradDiv());
		experience.setGraduatedIn(form.getGraduatedIn());
		experience.setDegree(form.getDegree());
		experience.setPosition(form.getPosition());
		experience.setClub(form.getClub());
		experience.setOffer(form.getOffer());
		if (!(Objects.equals(form.getEs(), null) || Objects.equals(form.getInterview(), null))) {
			experience.setEs(this.convertEsFormIntoEs(form.getEs()));
			experience.setInterview(this.convertInterviewFormIntoInterview(form.getInterview()));	
		}
		return experience;
	}
	
	public List<Es> convertEsFormIntoEs(List<EsForm> form) {
		List<Es> es = new ArrayList<Es>();
		form.forEach(formItem -> {;
			es.add(this.convertEsFormIntoEs(formItem));
		});
		return es;
	}
	
	public List<Interview> convertInterviewFormIntoInterview(List<InterviewForm> form) {
		List<Interview> interview = new ArrayList<Interview>();
		form.forEach(formItem -> {;
			interview.add(this.convertInterviewFormIntoInterview(formItem));
		});
		return interview;
	}
	
	public Es convertEsFormIntoEs(EsForm form) {
		Es es = new Es();
		es.setEs_id(form.getEsId());
		es.setCorp(form.getCorp());
		es.setResult(form.getResult());
		es.getQuestion().add(form.getQuestion());
		es.getAnswer().add(form.getAnswer());
		es.getAdvice().add(form.getAdvice());
		return es;
	}
	
	public Interview convertInterviewFormIntoInterview(InterviewForm form) {
		Interview interview = new Interview();
		interview.setInterview_id(form.getInterviewId());
		interview.setQuestion(form.getQuestion());
		interview.setAnswer(form.getAnswer());
		return interview;
	}
	
	public ExperienceForm convertExperienceIntoExperienceForm(Experience experience) {
		if (Objects.equals(experience, null)) {
			return new ExperienceForm();
		}
		ExperienceForm form = new ExperienceForm();
		form.setFamilyName(experience.getFamilyName());
		form.setGivenName(experience.getGivenName());
		form.setKanaFamilyName(experience.getKanaFamilyName());
		form.setKanaGivenName(experience.getKanaGivenName());
		form.setSex(experience.getSex());
		form.setMajor(experience.getMajor());
		form.setUnivLoc(experience.getUnivLoc());
		form.setUnivName(experience.getUnivName());
		form.setUnivFac(experience.getUnivFac());
		form.setUnivDep(experience.getUnivDep());
		form.setGradLoc(experience.getGradLoc());
		form.setGradName(experience.getGradName());
		form.setGradSchool(experience.getGradSchool());
		form.setGradDiv(experience.getGradDiv());
		form.setGraduatedIn(experience.getGraduatedIn());
		form.setDegree(experience.getDegree());
		form.setPosition(experience.getPosition());
		form.setClub(experience.getClub());
		form.setOffer(experience.getOffer());
		if (!(Objects.equals(experience.getEs(), null) || Objects.equals(experience.getInterview(), null))) {
			form.setEs(this.convertEsIntoEsForm(experience.getEs()));
			form.setInterview(this.convertInterviewIntoInterviewForm(experience.getInterview()));
		}
		return form;
	}
	
	public List<EsForm> convertEsIntoEsForm(List<Es> es) {
		if (Objects.equals(es, null)) {
			return new ArrayList<EsForm>();
		}
		List<EsForm> form = new ArrayList<EsForm>();
		es.forEach(esItem -> {;
			form.add(this.convertEsIntoEsForm(esItem));
		});
		return form;
	}
	
	public List<InterviewForm> convertInterviewIntoInterviewForm(List<Interview> interview) {
		if (Objects.equals(interview, null)) {
			return new ArrayList<InterviewForm>();
		}
		List<InterviewForm> form = new ArrayList<InterviewForm>();
		interview.forEach(interviewItem -> {;
			form.add(this.convertInterviewIntoInterviewForm(interviewItem));
		});
		return form;
	}
	
	public EsForm convertEsIntoEsForm(Es es) {
		if (Objects.equals(es, null)) {
			return new EsForm();
		}
		EsForm form = new EsForm();
		form.setEsId(es.getEs_id());
		form.setCorp(es.getCorp());
		form.setResult(es.getResult());
		form.setQuestion(es.getQuestion().get(0));
		form.setAnswer(es.getAnswer().get(0));
		form.setAdvice(es.getAdvice().get(0));
		return form;
	}
	
	public InterviewForm convertInterviewIntoInterviewForm(Interview interview) {
		if (Objects.equals(interview, null)) {
			return new InterviewForm();
		}
		InterviewForm form = new InterviewForm();
		form.setInterviewId(interview.getInterview_id());
		form.setQuestion(interview.getQuestion());
		form.setAnswer(interview.getAnswer());
		return form;
	}
}
