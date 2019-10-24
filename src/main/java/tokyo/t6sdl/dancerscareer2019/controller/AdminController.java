package tokyo.t6sdl.dancerscareer2019.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.io.ExcelBuilder;
import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.Student;
import tokyo.t6sdl.dancerscareer2019.model.form.EsForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ExperienceForm;
import tokyo.t6sdl.dancerscareer2019.model.form.InterviewForm;
import tokyo.t6sdl.dancerscareer2019.model.form.SearchForm;
import tokyo.t6sdl.dancerscareer2019.service.ExperienceService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
	private final ProfileService profileService;
	private final ExperienceService experienceService;
	
	@RequestMapping()
	public String getAdmin() {
		return "admin/index";
	}
	
	@RequestMapping(value="/search/students", params="all")
	public String getSearchStudents(@RequestParam(name="sort") String sort, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(new SearchForm(sort));
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = profileService.getProfiles(sortId);
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		return "admin/students/search";
	}
	
	@RequestMapping(value="/search/students/download", params="all")
	public ModelAndView getStudentsData(@RequestParam(name="sort") String sort) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("なし");
		@SuppressWarnings("unchecked")
		List<Student> students = (List<Student>) profileService.getProfiles(sortId).get("students");
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_all" + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="by-name")
	public String getSearchStudentsByName(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getKanaFirstName().isEmpty()) && !(form.getKanaLastName().isEmpty())) {
			result = profileService.getProfilesByName(sortId, form.getKanaFirstName(), form.getKanaFirstName());
		} else if (!(form.getKanaLastName().isEmpty())) {
			result = profileService.getProfilesByLastName(sortId, form.getKanaLastName());
		} else {
			result = profileService.getProfiles(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		return "admin/students/search";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/search/students/download", params="by-name")
	public ModelAndView getStudentsDataByName(@RequestParam(name="sort") String sort, @RequestParam(name="kanaLastName") String kanaLastName, @RequestParam(name="kanaFirstName", required=false) String kanaFirstName) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("氏名(カナ)", "セイ", kanaLastName, "メイ", kanaFirstName);
		List<Student> students = new ArrayList<Student>();
		if (!(kanaFirstName.isEmpty()) && !(kanaLastName.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByName(sortId, kanaLastName, kanaFirstName).get("students");
		} else if (!(kanaLastName.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByLastName(sortId, kanaLastName).get("students");
		} else {
			students = (List<Student>) profileService.getProfiles(sortId);
		}
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_byname" + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="by-university")
	public String getSearchStudentsByUniveristy(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute("hiddenUnivPref", form.getUnivPref());
		model.addAttribute("hiddenUnivName", form.getUnivName());
		model.addAttribute("hiddenFac", form.getFaculty());
		model.addAttribute("hiddenDep", form.getDepartment());
		model.addAttribute("hiddenGradSchoolPref", form.getGradSchoolPref());
		model.addAttribute("hiddenGradSchoolName", form.getGradSchoolName());
		model.addAttribute("hiddenGradSchoolOf", form.getGradSchoolOf());
		model.addAttribute("hiddenProgramIn", form.getProgramIn());
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getDepartment().isEmpty())) {
			result = profileService.getProfilesByDepartment(sortId, form.getUnivPref(), form.getUnivName(), form.getFaculty(), form.getDepartment());
		} else if (!(form.getFaculty().isEmpty())) {
			result = profileService.getProfilesByFaculty(sortId, form.getUnivPref(), form.getUnivName(), form.getFaculty());
		} else if (!(form.getUnivName().isEmpty())) {
			result = profileService.getProfilesByUniversity(sortId, form.getUnivPref(), form.getUnivName());
		} else if (!(form.getUnivPref().isEmpty())) {
			result = profileService.getProfilesByPrefecture(sortId, form.getUnivPref());
		} else {
			result = profileService.getProfiles(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		return "admin/students/search";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/search/students/download", params="by-university")
	public ModelAndView getStudentsDataByUniveristy(@RequestParam(name="sort") String sort, @RequestParam(name="prefecture") String prefecture, @RequestParam(name="university", required=false) String university, @RequestParam(name="faculty", required=false) String faculty, @RequestParam(name="department", required=false) String department) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("大学", "大学所在地", prefecture, "大学名", university, "学部名", faculty, "学科名", department);
		List<Student> students = new ArrayList<Student>();
		if (!(department.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByDepartment(sortId, prefecture, university, faculty, department).get("students");
		} else if (!(faculty.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByFaculty(sortId, prefecture, university, faculty).get("students");
		} else if (!(university.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByUniversity(sortId, prefecture, university).get("students");
		} else if (!(prefecture.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByPrefecture(sortId, prefecture).get("students");
		} else {
			students = (List<Student>) profileService.getProfiles(sortId);
		}
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_byuniv" + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="and-search-by-position")
	public String getAndSearchStudentsByPosition(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getPosition().isEmpty()) && !(form.getPosition().get(0).isEmpty())) {
			result = profileService.getProfilesByPosition(sortId, form.getPosition(), true);
		} else {
			result = profileService.getProfiles(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		return "admin/students/search";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/search/students/download", params="and-search-by-position")
	public ModelAndView getAndStudentsDataByPosition(@RequestParam(name="sort") String sort, @RequestParam(name="position") List<String> position) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = new ArrayList<String>(Arrays.asList("役職(AND検索)"));
		for (int i = 0; i < position.size(); i++) {
			String num = String.valueOf(i + 1);
			filter.add("役職 " + num);
			filter.add(position.get(i));
		}
		List<Student> students = new ArrayList<Student>();
		if (!(position.isEmpty()) && !(position.get(0).isEmpty())) {
			students = (List<Student>) profileService.getProfilesByPosition(sortId, position, true).get("students");
		} else {
			students = (List<Student>) profileService.getProfiles(sortId);
		}
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_bypos(and)" + ".xlsx"), map);
		return mav;
	}
	
	@RequestMapping(value="/search/students", params="or-search-by-position")
	public String getOrSearchStudentsByPosition(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getPosition().isEmpty()) && !(form.getPosition().get(0).isEmpty())) {
			result = profileService.getProfilesByPosition(sortId, form.getPosition(), false);
		} else {
			result = profileService.getProfiles(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		return "admin/students/search";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/search/students/download", params="or-search-by-position")
	public ModelAndView getOrStudentsDataByPosition(@RequestParam(name="sort") String sort, @RequestParam(name="position") List<String> position) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = new ArrayList<String>(Arrays.asList("役職(OR検索)"));
		for (int i = 0; i < position.size(); i++) {
			String num = String.valueOf(i + 1);
			filter.add("役職 " + num);
			filter.add(position.get(i));
		}
		List<Student> students = new ArrayList<Student>();
		if (!(position.isEmpty()) && !(position.get(0).isEmpty())) {
			students = (List<Student>) profileService.getProfilesByPosition(sortId, position, true).get("students");
		} else {
			students = (List<Student>) profileService.getProfiles(sortId);
		}
		students.sort(Comparator.comparing(Student::getLast_login, Comparator.reverseOrder()));
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_bypos(or)" + ".xlsx"), map);
		return mav;
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
		return "redirect:/admin/search/experiences?all&sort=0";
	}
	
	@GetMapping(value="/experiences/{experienceId}", params="modify")
	public String getModifyExperiences(@PathVariable(name="experienceId") String experienceId, Model model) {
		if (experienceId.equals("new")) {
			return "redirect:/admin";
		}
		model.addAttribute("positionList", Profile.POSITION_LIST);
		int id = Integer.parseInt(experienceId);
		ExperienceForm form = experienceService.convertExperienceIntoExperienceForm(experienceService.getExperienceById(id, false, false));
		model.addAttribute("hiddenUnivPref", form.getUnivPref());
		model.addAttribute("hiddenUnivName", form.getUnivName());
		model.addAttribute("hiddenFac", form.getFaculty());
		model.addAttribute("hiddenDep", form.getDepartment());
		model.addAttribute("hiddenGradSchoolPref", form.getGradSchoolPref());
		model.addAttribute("hiddenGradSchoolName", form.getGradSchoolName());
		model.addAttribute("hiddenGradSchoolOf", form.getGradSchoolOf());
		model.addAttribute("hiddenProgramIn", form.getProgramIn());
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
					model.addAttribute("hiddenUnivPref", form.getUnivPref());
					model.addAttribute("hiddenUnivName", form.getUnivName());
					model.addAttribute("hiddenFac", form.getFaculty());
					model.addAttribute("hiddenDep", form.getDepartment());
					model.addAttribute("hiddenGradSchoolPref", form.getGradSchoolPref());
					model.addAttribute("hiddenGradSchoolName", form.getGradSchoolName());
					model.addAttribute("hiddenGradSchoolOf", form.getGradSchoolOf());
					model.addAttribute("hiddenProgramIn", form.getProgramIn());
					return "admin/experiences/submit";
				} else {
					model.addAttribute(form);
					return "admin/experiences/confirm";
				}
			default:
				form.setClub(this.cleanUp(form.getClub(), ""));
				form.setOffer(this.cleanUp(form.getOffer(), ""));
				if (result.hasErrors()) {
					model.addAttribute("positionList", Profile.POSITION_LIST);
					model.addAttribute("hiddenUnivPref", form.getUnivPref());
					model.addAttribute("hiddenUnivName", form.getUnivName());
					model.addAttribute("hiddenFac", form.getFaculty());
					model.addAttribute("hiddenDep", form.getDepartment());
					model.addAttribute("hiddenGradSchoolPref", form.getGradSchoolPref());
					model.addAttribute("hiddenGradSchoolName", form.getGradSchoolName());
					model.addAttribute("hiddenGradSchoolOf", form.getGradSchoolOf());
					model.addAttribute("hiddenProgramIn", form.getProgramIn());
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
		model.addAttribute("hiddenUnivPref", form.getUnivPref());
		model.addAttribute("hiddenUnivName", form.getUnivName());
		model.addAttribute("hiddenFac", form.getFaculty());
		model.addAttribute("hiddenDep", form.getDepartment());
		model.addAttribute("hiddenGradSchoolPref", form.getGradSchoolPref());
		model.addAttribute("hiddenGradSchoolName", form.getGradSchoolName());
		model.addAttribute("hiddenGradSchoolOf", form.getGradSchoolOf());
		model.addAttribute("hiddenProgramIn", form.getProgramIn());
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
	public String getSearchExperiences(@RequestParam(name="sort") String sort, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(new SearchForm(sort));
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = experienceService.getExperiences(sortId);
		model.addAttribute("count", result.get("count"));
		model.addAttribute("experiences", result.get("experiences"));
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="by-name")
	public String getSearchExperiencesByName(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getKanaLastName().isEmpty()) && !(form.getKanaFirstName().isEmpty())) {
			result = experienceService.getExperiencesByName(sortId, form.getKanaLastName(), form.getKanaFirstName());
		} else if (!(form.getKanaLastName().isEmpty())) {
			result = experienceService.getExperiencesByLastName(sortId, form.getKanaLastName());
		} else {
			result = experienceService.getExperiences(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("experiences", result.get("experiences"));
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="by-university")
	public String getSearchExperiencesByUniveristy(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute("hiddenUnivPref", form.getUnivPref());
		model.addAttribute("hiddenUnivName", form.getUnivName());
		model.addAttribute("hiddenFac", form.getFaculty());
		model.addAttribute("hiddenDep", form.getDepartment());
		model.addAttribute("hiddenGradSchoolPref", form.getGradSchoolPref());
		model.addAttribute("hiddenGradSchoolName", form.getGradSchoolName());
		model.addAttribute("hiddenGradSchoolOf", form.getGradSchoolOf());
		model.addAttribute("hiddenProgramIn", form.getProgramIn());
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getDepartment().isEmpty())) {
			result = experienceService.getExperiencesByDepartment(sortId, form.getUnivPref(), form.getUnivName(), form.getFaculty(), form.getDepartment());
		} else if (!(form.getFaculty().isEmpty())) {
			result = experienceService.getExperiencesByFaculty(sortId, form.getUnivPref(), form.getUnivName(), form.getFaculty());
		} else if (!(form.getUnivName().isEmpty())) {
			result = experienceService.getExperiencesByUniversity(sortId, form.getUnivPref(), form.getUnivName());
		} else if (!(form.getUnivPref().isEmpty())) {
			result = experienceService.getExperiencesByPrefecture(sortId, form.getUnivPref());
		} else {
			result = experienceService.getExperiences(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("experiences", result.get("experiences"));
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="and-search-by-position")
	public String getAndSearchExperiencesByPosition(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getPosition().isEmpty()) && !(form.getPosition().get(0).isEmpty())) {
			result = experienceService.getExperiencesByPosition(sortId, form.getPosition(), true);
		} else {
			result = experienceService.getExperiences(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("experiences", result.get("experiences"));
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/search/experiences", params="or-search-by-position")
	public String getOrSearchExperiencesByPosition(SearchForm form, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(form);
		int sortId;
		try {
			sortId = Integer.parseInt(form.getSort());
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(form.getPosition().isEmpty()) && !(form.getPosition().get(0).isEmpty())) {
			result = experienceService.getExperiencesByPosition(sortId, form.getPosition(), false);
		} else {
			result = experienceService.getExperiences(sortId);
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("experiences", result.get("experiences"));
		return "admin/experiences/search";
	}
	
	@RequestMapping(value="/mail", params="develop")
	public String redirectToIndex() {
		return "redirect:/admin";
	}
}
