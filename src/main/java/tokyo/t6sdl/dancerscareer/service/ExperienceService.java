package tokyo.t6sdl.dancerscareer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.model.Es;
import tokyo.t6sdl.dancerscareer.model.Experience;
import tokyo.t6sdl.dancerscareer.model.Interview;
import tokyo.t6sdl.dancerscareer.model.form.EsForm;
import tokyo.t6sdl.dancerscareer.model.form.ExperienceForm;
import tokyo.t6sdl.dancerscareer.model.form.InterviewForm;
import tokyo.t6sdl.dancerscareer.repository.ExperienceRepository;

@RequiredArgsConstructor
@Service
public class ExperienceService {
	private final ExperienceRepository experienceRepository;

	public Experience getExperienceById(int expId, boolean all, boolean pvCount) {
		return experienceRepository.findOneById(expId, all, pvCount);
	}

	public Experience getALittleExperienceById(int expId) {
		return experienceRepository.findALittleOneById(expId);
	}

	public Map<String, Object> getExperiences(int sort) {
		return experienceRepository.find(sort);
	}

	public Map<String, Object> getExperiencesByName(int sort, String kanaFamilyName, String kanaGivenName) {
		return experienceRepository.findByName(sort, kanaFamilyName, kanaGivenName);
	}

	public Map<String, Object> getExperiencesByFamilyName(int sort, String kanaFamilyName) {
		return experienceRepository.findByFamilyName(sort, kanaFamilyName);
	}

	public Map<String, Object> getExperiencesByUnivLoc(int sort, String univLoc) {
		return experienceRepository.findByUnivLoc(sort, univLoc);
	}

	public Map<String, Object> getExperiencesByUnivName(int sort, String univLoc, String univName) {
		return experienceRepository.findByUnivName(sort, univLoc, univName);
	}

	public Map<String, Object> getExperiencesByUnivFac(int sort, String univLoc, String univName, String univFac) {
		return experienceRepository.findByUnivFac(sort, univLoc, univName, univFac);
	}

	public Map<String, Object> getExperiencesByUnivDep(int sort, String univLoc, String univName, String univFac, String univDep) {
		return experienceRepository.findByUnivDep(sort, univLoc, univName, univFac, univDep);
	}

	public Map<String, Object> getExperiencesByPosition(int sort, List<String> position, boolean andSearch) {
		return experienceRepository.findByPosition(sort, position, andSearch);
	}

	public Map<String, Object> getExperiencesByCreatedAt() {
		return experienceRepository.findByCreatedAt();
	}

	public Es getEsById(int expId, int esId) {
		return experienceRepository.findEsById(expId, esId);
	}

	public Interview getInterviewById(int expId, int itvId) {
		return experienceRepository.findInterviewById(expId, itvId);
	}

	public void register(Experience newExperience) {
		experienceRepository.insert(newExperience);
	}

	public void delete(int expId) {
		experienceRepository.delete(expId);
	}

	public void update(Experience experience) {
		experienceRepository.update(experience);
	}

	public void updateLikes(int expId, boolean increment) {
		experienceRepository.updateLikes(expId, increment);
	}

	public void registerEs(Es newEs) {
		experienceRepository.insertEs(newEs);
	}

	public void deleteEs(int expId, int esId) {
		experienceRepository.deleteEs(expId, esId);
	}

	public void updateEs(Es es) {
		experienceRepository.updateEs(es);
	}

	public void registerInterview(Interview newInterview) {
		experienceRepository.insertInterview(newInterview);
	}

	public void deleteInterview(int expId, int itvId) {
		experienceRepository.deleteInterview(expId, itvId);
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
		es.setId(form.getId());
		es.setCorp(form.getCorp());
		es.setResult(form.getResult());
		es.getQuestion().add(form.getQuestion());
		es.getAnswer().add(form.getAnswer());
		es.getAdvice().add(form.getAdvice());
		return es;
	}

	public Interview convertInterviewFormIntoInterview(InterviewForm form) {
		Interview interview = new Interview();
		interview.setId(form.getId());
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
		form.setId(es.getId());
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
		form.setId(interview.getId());
		form.setQuestion(interview.getQuestion());
		form.setAnswer(interview.getAnswer());
		return form;
	}
}
