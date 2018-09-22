package tokyo.t6sdl.dancerscareer2019.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.io.ExcelBuilder;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.Student;
import tokyo.t6sdl.dancerscareer2019.model.form.EsForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ExperienceForm;
import tokyo.t6sdl.dancerscareer2019.model.form.InterviewForm;
import tokyo.t6sdl.dancerscareer2019.model.form.SearchForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.ExperienceService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
	private final ProfileService profileService;
	private final AccountService accountService;
	private final ExperienceService experienceService;
	
	@RequestMapping()
	public String getAdmin() {
		return "admin/index";
	}
	
	@RequestMapping(value="/search/students", params="all")
	public String getSearchStudents(Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(new SearchForm());
		List<Profile> profiles = profileService.getProfiles();
		List<Student> students = this.makeStudentOfProfile(profiles);
		List<String> emails = new ArrayList<String>();
		students.forEach(student -> {
			emails.add(student.getEmail());
			student.convertForDisplay();
		});
		model.addAttribute("students", students);
		model.addAttribute("emails", emails);
		return "admin/students/search";
	}
	
	@RequestMapping(value="/search/students/download", params="all")
	public ModelAndView getStudentsData() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Student> students = this.makeStudentOfProfile(profileService.getProfiles());
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now();
		String today = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder("students_all_" + today + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="by-name")
	public String getSearchStudentsByName(@RequestParam(name="kanaLastName") String kanaLastName, @RequestParam(name="kanaFirstName", required=false) String kanaFirstName, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setKanaLastName(kanaLastName);
		form.setKanaFirstName(kanaFirstName);
		model.addAttribute(form);
		List<Profile> profiles = new ArrayList<Profile>();
		if (kanaFirstName.isEmpty()) {
			profiles = profileService.getProfilesByLastName(kanaLastName);
		} else {
			profiles = profileService.getProfilesByName(kanaLastName, kanaFirstName);
		}
		List<Student> students = this.makeStudentOfProfile(profiles);
		List<String> emails = new ArrayList<String>();
		students.forEach(student -> {
			emails.add(student.getEmail());
			student.convertForDisplay();
		});
		model.addAttribute("students", students);
		model.addAttribute("emails", emails);
		return "admin/students/search";
	}
	
	@RequestMapping(value="/search/students/download", params="by-name")
	public ModelAndView getStudentsDataByName(@RequestParam(name="kanaLastName") String kanaLastName, @RequestParam(name="kanaFirstName", required=false) String kanaFirstName) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Profile> profiles = new ArrayList<Profile>();
		if (kanaFirstName.isEmpty()) {
			profiles = profileService.getProfilesByLastName(kanaLastName);
		} else {
			profiles = profileService.getProfilesByName(kanaLastName, kanaFirstName);
		}
		List<Student> students = this.makeStudentOfProfile(profiles);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now();
		String today = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder("students_byname_" + today + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="by-university")
	public String getSearchStudentsByUniveristy(@RequestParam(name="prefecture") String prefecture, @RequestParam(name="university", required=false) String university, @RequestParam(name="faculty", required=false) String faculty, @RequestParam(name="department", required=false) String department, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setPrefecture(prefecture);
		form.setUniversity(university);
		form.setFaculty(faculty);
		form.setDepartment(department);
		model.addAttribute("hiddenPref", prefecture);
		model.addAttribute("hiddenUniv", university);
		model.addAttribute("hiddenFac", faculty);
		model.addAttribute("hiddenDep", department);
		model.addAttribute(form);
		List<Profile> profiles = new ArrayList<Profile>();
		if (!(department.isEmpty())) {
			profiles = profileService.getProfilesByDepartment(prefecture, university, faculty, department);
		} else if (!(faculty.isEmpty())) {
			profiles = profileService.getProfilesByFaculty(prefecture, university, faculty);
		} else if (!(university.isEmpty())) {
			profiles = profileService.getProfilesByUniversity(prefecture, university);
		} else {
			profiles = profileService.getProfilesByPrefecture(prefecture);
		}
		List<Student> students = this.makeStudentOfProfile(profiles);
		List<String> emails = new ArrayList<String>();
		students.forEach(student -> {
			emails.add(student.getEmail());
			student.convertForDisplay();
		});
		model.addAttribute("students", students);
		model.addAttribute("emails", emails);
		return "admin/students/search";
	}
	
	@RequestMapping(value="/search/students/download", params="by-university")
	public ModelAndView getStudentsDataByUniveristy(@RequestParam(name="prefecture") String prefecture, @RequestParam(name="university", required=false) String university, @RequestParam(name="faculty", required=false) String faculty, @RequestParam(name="department", required=false) String department) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Profile> profiles = new ArrayList<Profile>();
		if (!(department.isEmpty())) {
			profiles = profileService.getProfilesByDepartment(prefecture, university, faculty, department);
		} else if (!(faculty.isEmpty())) {
			profiles = profileService.getProfilesByFaculty(prefecture, university, faculty);
		} else if (!(university.isEmpty())) {
			profiles = profileService.getProfilesByUniversity(prefecture, university);
		} else {
			profiles = profileService.getProfilesByPrefecture(prefecture);
		}
		List<Student> students = this.makeStudentOfProfile(profiles);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now();
		String today = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder("students_byuniv_" + today + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="and-search-by-position")
	public String getAndSearchStudentsByPosition(@RequestParam(name="position") List<String> position, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setPosition(position);
		model.addAttribute(form);
		List<Profile> profiles = profileService.getProfilesByPosition(position, "AND");
		List<Student> students = this.makeStudentOfProfile(profiles);
		List<String> emails = new ArrayList<String>();
		students.forEach(student -> {
			emails.add(student.getEmail());
			student.convertForDisplay();
		});
		model.addAttribute("students", students);
		model.addAttribute("emails", emails);
		return "admin/students/search";
	}
	
	@RequestMapping(value="/search/students/download", params="and-search-by-position")
	public ModelAndView getAndStudentsDataByPosition(@RequestParam(name="position") List<String> position) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Student> students = this.makeStudentOfProfile(profileService.getProfilesByPosition(position, "AND"));
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now();
		String today = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder("students_byandpos_" + today + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="or-search-by-position")
	public String getOrSearchStudentsByPosition(@RequestParam(name="position") List<String> position, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setPosition(position);
		model.addAttribute(form);
		List<Profile> profiles = profileService.getProfilesByPosition(position, "OR");
		List<Student> students = this.makeStudentOfProfile(profiles);
		List<String> emails = new ArrayList<String>();
		students.forEach(student -> {
			emails.add(student.getEmail());
			student.convertForDisplay();
		});
		model.addAttribute("students", students);
		model.addAttribute("emails", emails);
		return "admin/students/search";
	}
	
	@RequestMapping(value="/search/students/download", params="or-search-by-position")
	public ModelAndView getOrStudentsDataByPosition(@RequestParam(name="position") List<String> position) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Student> students = this.makeStudentOfProfile(profileService.getProfilesByPosition(position, "OR"));
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now();
		String today = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder("students_byorpos_" + today + ".xlsx"), map);
		return mav;
	}
		
	private List<Student> makeStudentOfProfile(List<Profile> profiles) {
		List<Student> students = new ArrayList<Student>();
		profiles.forEach(profile -> {
			Student student = profileService.convertProfileIntoStudent(profile);
			Account account = accountService.getAccountByEmail(profile.getEmail());
			student.setValid_email(account.isValid_email());
			student.setLast_login(account.getLast_login());
			students.add(student);
		});
		return students;
	}
	
	@GetMapping(value="/experiences/{experienceId}")
	public String getSubmitExperiences(@PathVariable(name="experienceId") String experienceId, Model model) {
		model.addAttribute("experienceId", experienceId);
		model.addAttribute("positionList", Profile.POSITION_LIST);
		if (experienceId.equals("new")) {
			ExperienceForm form = new ExperienceForm();
			form.init();
			model.addAttribute(form);
			return "admin/experiences/submit";
		} else {
			int id = Integer.parseInt(experienceId);
			Experience experience = experienceService.getExperienceById(id, true, false);
			model.addAttribute(experience);
			return "admin/experiences/detail";
		}
	}
	
	@GetMapping(value="/experiences/{experienceId}", params="delete")
	public String getDeleteExperiences(@PathVariable(name="experienceId") String experienceId, Model model) {
		if (experienceId.equals("new")) {
			return "redirect:/admin";
		}
		int id = Integer.parseInt(experienceId);
		experienceService.delete(id);
		return "redirect:/admin/search/experiences?all";
	}
	
	@GetMapping(value="/experiences/{experienceId}", params="modify")
	public String getModifyExperiences(@PathVariable(name="experienceId") String experienceId, Model model) {
		if (experienceId.equals("new")) {
			return "redirect:/admin";
		}
		model.addAttribute("positionList", Profile.POSITION_LIST);
		int id = Integer.parseInt(experienceId);
		ExperienceForm form = experienceService.convertExperienceIntoExperienceForm(experienceService.getExperienceById(id, false, false));
		model.addAttribute("hiddenPref", form.getPrefecture());
		model.addAttribute("hiddenUniv", form.getUniversity());
		model.addAttribute("hiddenFac", form.getFaculty());
		model.addAttribute("hiddenDep", form.getDepartment());
		model.addAttribute(form);
		return "admin/experiences/modify";
	}
	
	@PostMapping(value="/experiences/{experienceId}", params="post")
	public String postSubmitExperiences(@PathVariable(name="experienceId") String experienceId, @Validated ExperienceForm form, BindingResult result, Model model) {
		model.addAttribute("experienceId", experienceId);
		switch (experienceId) {
			case "new":
				form.setClub(this.cleanUp(form.getClub(), ""));
				form.setOffer(this.cleanUp(form.getOffer(), ""));
				form.setEs(this.cleanUp(form.getEs(), new EsForm()));
				form.setInterview(this.cleanUp(form.getInterview(), new InterviewForm()));
				if (result.hasErrors()) {
					model.addAttribute("positionList", Profile.POSITION_LIST);
					model.addAttribute("hiddenPref", form.getPrefecture());
					model.addAttribute("hiddenUniv", form.getUniversity());
					model.addAttribute("hiddenFac", form.getFaculty());
					model.addAttribute("hiddenDep", form.getDepartment());
					return "admin/experiences/submit";
				} else {
					model.addAttribute(form);
					return "admin/experiences/confirm";
				}
			default:
				if (result.hasErrors()) {
					model.addAttribute("positionList", Profile.POSITION_LIST);
					model.addAttribute("hiddenPref", form.getPrefecture());
					model.addAttribute("hiddenUniv", form.getUniversity());
					model.addAttribute("hiddenFac", form.getFaculty());
					model.addAttribute("hiddenDep", form.getDepartment());
					return "admin/experiences/modify";
				} else {
					int experience_id = Integer.parseInt(experienceId);
					Experience experience = experienceService.convertExperienceFormIntoExperience(form);
					experience.setExperience_id(experience_id);
					experienceService.update(experience);
					return "redirect:/admin/experiences/" + experienceId;
				}
		}
	}
	
	@PostMapping(value="/experiences/new", params="modify")
	public String postNotCompleteExperiences(@Validated ExperienceForm form, BindingResult result, Model model) {
		model.addAttribute("experienceId", "new");
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute("hiddenPref", form.getPrefecture());
		model.addAttribute("hiddenUniv", form.getUniversity());
		model.addAttribute("hiddenFac", form.getFaculty());
		model.addAttribute("hiddenDep", form.getDepartment());
		model.addAttribute(form);
		return "admin/experiences/submit";
	}
	
	@PostMapping(value="/experiences/new", params="complete")
	public String postCompleteExperiences(@Validated ExperienceForm form, BindingResult result, Model model) {
		Experience newExperience = experienceService.convertExperienceFormIntoExperience(form);
		experienceService.register(newExperience);
		return "admin/experiences/complete";
	}
	
	@GetMapping("/experiences/{experienceId}/es/new")
	public String getSubmitEs(@PathVariable(name="experienceId") String experienceId, Model model) {
		model.addAttribute("experienceId", experienceId);
		model.addAttribute("esId", "new");
		model.addAttribute(new EsForm());
		return "admin/experiences/modifyEs";
	}
	
	@GetMapping(value="/experiences/{experienceId}/es/{esId}", params="modify")
	public String getModifyEs(@PathVariable(name="experienceId") String experienceId, @PathVariable(name="esId") String esId, Model model) {
		model.addAttribute("experienceId", experienceId);
		model.addAttribute("esId", esId);
		int experience_id = Integer.parseInt(experienceId);
		int es_id = Integer.parseInt(esId);
		EsForm form = experienceService.convertEsIntoEsForm(experienceService.getEsById(experience_id, es_id));
		model.addAttribute(form);
		return "admin/experiences/modifyEs";
	}
		
	@PostMapping(value="/experiences/{experienceId}/es/{esId}", params="post")
	public String postSubmitEs(@PathVariable(name="experienceId") String experienceId, @PathVariable(name="esId") String esId, EsForm form, BindingResult result, Model model) {
		Es es = experienceService.convertEsFormIntoEs(form);
		int experience_id = Integer.parseInt(experienceId);
		es.setExperience_id(experience_id);
		if (esId.equals("new")) {
			experienceService.registerEs(es);
		} else {
			int es_id = Integer.parseInt(esId);
			es.setEs_id(es_id);
			experienceService.updateEs(es);
		}
		return "redirect:/admin/experiences/" + experienceId;
	}
	
	@GetMapping(value="/experiences/{experienceId}/es/{esId}", params="delete")
	public String getDeleteEs(@PathVariable(name="experienceId") String experienceId, @PathVariable(name="esId") String esId, Model model) {
		int experience_id = Integer.parseInt(experienceId);
		int es_id = Integer.parseInt(esId);
		experienceService.deleteEs(experience_id, es_id);
		return "redirect:/admin/experiences/" + experienceId;
	}
	
	@GetMapping("/experiences/{experienceId}/interview/new")
	public String getSubmitInterview(@PathVariable(name="experienceId") String experienceId, Model model) {
		model.addAttribute("experienceId", experienceId);
		model.addAttribute("interviewId", "new");
		model.addAttribute(new InterviewForm());
		return "admin/experiences/modifyInterview";
	}
	
	@GetMapping(value="/experiences/{experienceId}/interview/{interviewId}", params="modify")
	public String getModifyInterview(@PathVariable(name="experienceId") String experienceId, @PathVariable(name="interviewId") String interviewId, Model model) {
		model.addAttribute("experienceId", experienceId);
		model.addAttribute("interviewId", interviewId);
		int experience_id = Integer.parseInt(experienceId);
		int interview_id = Integer.parseInt(interviewId);
		InterviewForm form = experienceService.convertInterviewIntoInterviewForm(experienceService.getInterviewById(experience_id, interview_id));
		model.addAttribute(form);
		return "admin/experiences/modifyInterview";
	}
		
	@PostMapping(value="/experiences/{experienceId}/interview/{interviewId}", params="post")
	public String postSubmitInterview(@PathVariable(name="experienceId") String experienceId, @PathVariable(name="interviewId") String interviewId, InterviewForm form, BindingResult result, Model model) {
		Interview interview = experienceService.convertInterviewFormIntoInterview(form);
		int experience_id = Integer.parseInt(experienceId);
		interview.setExperience_id(experience_id);
		if (interviewId.equals("new")) {
			experienceService.registerInterview(interview);
		} else {
			int interview_id = Integer.parseInt(interviewId);
			interview.setInterview_id(interview_id);
			experienceService.updateInterview(interview);
		}
		return "redirect:/admin/experiences/" + experienceId;
	}
	
	@GetMapping(value="/experiences/{experienceId}/interview/{interviewId}", params="delete")
	public String getDeleteInterview(@PathVariable(name="experienceId") String experienceId, @PathVariable(name="interviewId") String interviewId, Model model) {
		int experience_id = Integer.parseInt(experienceId);
		int interview_id = Integer.parseInt(interviewId);
		experienceService.deleteInterview(experience_id, interview_id);
		return "redirect:/admin/experiences/" + experienceId;
	}
	
	private <T> List<T> cleanUp(List<T> list, T empty) {
		ListIterator<T> listItr = list.listIterator();
		while (listItr.hasNext()) {
			T item = listItr.next();
			if (item.toString().isEmpty()) {
				listItr.remove();
			}
		}
		if (list.isEmpty()) {
			list.add(empty);
		}
		return list;
	}
		
	@RequestMapping(value="/search/experiences", params="all")
	public String getSearchExperiences(Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(new SearchForm());
		List<Experience> experiences = experienceService.getExperiences();
		model.addAttribute("experiences", experiences);
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="by-name")
	public String getSearchExperiencesByName(@RequestParam(name="kanaLastName") String kanaLastName, @RequestParam(name="kanaFirstName", required=false) String kanaFirstName, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setKanaLastName(kanaLastName);
		form.setKanaFirstName(kanaFirstName);
		model.addAttribute(form);
		List<Experience> experiences = new ArrayList<Experience>();
		if (kanaFirstName.isEmpty()) {
			experiences = experienceService.getExperiencesByLastName(kanaLastName);
		} else {
			experiences = experienceService.getExperiencesByName(kanaLastName, kanaFirstName);
		}
		model.addAttribute("experiences", experiences);
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="by-university")
	public String getSearchExperiencesByUniveristy(@RequestParam(name="prefecture") String prefecture, @RequestParam(name="university", required=false) String university, @RequestParam(name="faculty", required=false) String faculty, @RequestParam(name="department", required=false) String department, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setPrefecture(prefecture);
		form.setUniversity(university);
		form.setFaculty(faculty);
		form.setDepartment(department);
		model.addAttribute("hiddenPref", prefecture);
		model.addAttribute("hiddenUniv", university);
		model.addAttribute("hiddenFac", faculty);
		model.addAttribute("hiddenDep", department);
		model.addAttribute(form);
		List<Experience> experiences = new ArrayList<Experience>();
		if (!(department.isEmpty())) {
			experiences = experienceService.getExperiencesByDepartment(prefecture, university, faculty, department);
		} else if (!(faculty.isEmpty())) {
			experiences = experienceService.getExperiencesByFaculty(prefecture, university, faculty);
		} else if (!(university.isEmpty())) {
			experiences = experienceService.getExperiencesByUniversity(prefecture, university);
		} else {
			experiences = experienceService.getExperiencesByPrefecture(prefecture);
		}
		model.addAttribute("experiences", experiences);
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="and-search-by-position")
	public String getAndSearchExperiencesByPosition(@RequestParam(name="position") List<String> position, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setPosition(position);
		model.addAttribute(form);
		List<Experience> experiences = experienceService.getExperiencesByPosition(position, "AND");
		model.addAttribute("experiences", experiences);
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="or-search-by-position")
	public String getOrSearchExperiencesByPosition(@RequestParam(name="position") List<String> position, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		SearchForm form = new SearchForm();
		form.setPosition(position);
		model.addAttribute(form);
		List<Experience> experiences = experienceService.getExperiencesByPosition(position, "OR");
		model.addAttribute("experiences", experiences);
		return "admin/experiences/search";
	}
}
