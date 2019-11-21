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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping()
	public String index() {
		return "admin/index";
	}
	
	@GetMapping(value = "/users")
	public String usersIndex(@RequestParam(name = "sort", defaultValue = "0") String sort, Model model) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = profileService.getProfiles(sortId);
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		model.addAttribute("sortId", sortId);
		model.addAttribute("positionList", Profile.POSITION_LIST);
		return "admin/users/index";
	}
	
	@GetMapping(value = "/users", params = "download")
	public ModelAndView downloadUsersIndex(@RequestParam(name = "sort", defaultValue = "0") String sort) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("なし", "-");
		@SuppressWarnings("unchecked")
		List<Student> students = (List<Student>) profileService.getProfiles(sortId).get("students");
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students" + ".xlsx"), map);
		return mav;
	}
	
	@GetMapping(value = "/users", params = {"kana-family-name", "kana-given-name"})
	public String usersIndexFilteredByName(@RequestParam(name = "sort", defaultValue = "0") String sort, @RequestParam("kana-family-name") String kanaFamilyName, @RequestParam("kana-given-name") String kanaGivenName, Model model) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(kanaFamilyName.isEmpty()) && !(kanaGivenName.isEmpty())) {
			result = profileService.getProfilesByName(sortId, kanaFamilyName, kanaGivenName);
		} else if (!(kanaFamilyName.isEmpty())) {
			result = profileService.getProfilesByLastName(sortId, kanaFamilyName);
		} else {
			return "redirect:/admin/users?sort=" + sortId;
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		model.addAttribute("sortId", sortId);
		model.addAttribute("kanaFamilyName", kanaFamilyName);
		model.addAttribute("kanaGivenName", kanaGivenName);
		model.addAttribute("positionList", Profile.POSITION_LIST);
		return "admin/users/index";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/users", params = {"kana-family-name", "kana-given-name", "download"})
	public ModelAndView downloadUsersIndexFilteredByName(@RequestParam(name = "sort", defaultValue = "0") String sort, @RequestParam("kana-family-name") String kanaFamilyName, @RequestParam("kana-given-name") String kanaGivenName) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("氏名(カナ)", kanaFamilyName + "," + kanaGivenName);
		List<Student> students = new ArrayList<Student>();
		if (!(kanaFamilyName.isEmpty()) && !(kanaGivenName.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByName(sortId, kanaFamilyName, kanaGivenName).get("students");
		} else if (!(kanaFamilyName.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByLastName(sortId, kanaFamilyName).get("students");
		}
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_name" + ".xlsx"), map);
		return mav;
	}
	
	@GetMapping(value = "/users", params = {"univ-loc", "univ-type", "univ-name", "univ-fac", "univ-dep"})
	public String usersIndexFilteredByUniv(@RequestParam(name = "sort", defaultValue = "0") String sort, @RequestParam("univ-loc") String univLoc, @RequestParam("univ-name") String univName, @RequestParam("univ-fac") String univFac, @RequestParam("univ-dep") String univDep, Model model) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(univDep.isEmpty())) {
			result = profileService.getProfilesByDepartment(sortId, univLoc, univName, univFac, univDep);
		} else if (!(univFac.isEmpty())) {
			result = profileService.getProfilesByFaculty(sortId, univLoc, univName, univFac);
		} else if (!(univName.isEmpty())) {
			result = profileService.getProfilesByUniversity(sortId, univLoc, univName);
		} else if (!(univLoc.isEmpty())) {
			result = profileService.getProfilesByPrefecture(sortId, univLoc);
		} else {
			return "redirect:/admin/users?sort=" + sortId;
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		model.addAttribute("sortId", sortId);
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute("hiddenUnivLoc", univLoc);
		model.addAttribute("hiddenUnivName", univName);
		model.addAttribute("hiddenUnivFac", univFac);
		model.addAttribute("hiddenUnivDep", univDep);
		return "admin/users/index";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/users", params = {"univ-loc", "univ-type", "univ-name", "univ-fac", "univ-dep", "download"})
	public ModelAndView downloadUsersIndexFilteredByUniv(@RequestParam(name = "sort", defaultValue = "0") String sort, @RequestParam("univ-loc") String univLoc, @RequestParam("univ-name") String univName, @RequestParam("univ-fac") String univFac, @RequestParam("univ-dep") String univDep) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("大学", univLoc + "," + univName + "," + univFac + "," + univDep);
		List<Student> students = new ArrayList<Student>();
		if (!(univDep.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByDepartment(sortId, univLoc, univName, univFac, univDep).get("students");
		} else if (!(univFac.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByFaculty(sortId, univLoc, univName, univFac).get("students");
		} else if (!(univName.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByUniversity(sortId, univLoc, univName).get("students");
		} else if (!(univLoc.isEmpty())) {
			students = (List<Student>) profileService.getProfilesByPrefecture(sortId, univLoc).get("students");
		}
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_univ" + ".xlsx"), map);
		return mav;
	}
	
	@GetMapping(value = "/users", params = {"pos", "cond=and"})
	public String usersIndexFilteredByPosAndPos(@RequestParam(name = "sort", defaultValue = "0") String sort, @RequestParam("pos") List<String> positions, Model model) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(positions.isEmpty()) && !(positions.get(0).isEmpty())) {
			result = profileService.getProfilesByPosition(sortId, positions, true);
		} else {
			return "redirect:/admin/users?sort=" + sortId;
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		model.addAttribute("sortId", sortId);
		model.addAttribute("positions", positions);
		model.addAttribute("positionList", Profile.POSITION_LIST);
		return "admin/users/index";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/users", params = {"pos", "cond=and", "download"})
	public ModelAndView downloadUsersIndexFilteredByPosAndPos(@RequestParam(name="sort") String sort, @RequestParam(name="pos") List<String> positions) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("役職(AND)", String.join(",", positions));
		List<Student> students = new ArrayList<Student>();
		if (!(positions.isEmpty()) && !(positions.get(0).isEmpty())) {
			students = (List<Student>) profileService.getProfilesByPosition(sortId, positions, true).get("students");
		}
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_pos_and" + ".xlsx"), map);
		return mav;
	}
	
	@GetMapping(value = "/users", params = {"pos", "cond=or"})
	public String usersIndexFilteredByPosOrPos(@RequestParam(name = "sort", defaultValue = "0") String sort, @RequestParam("pos") List<String> positions, Model model) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (!(positions.isEmpty()) && !(positions.get(0).isEmpty())) {
			result = profileService.getProfilesByPosition(sortId, positions, false);
		} else {
			return "redirect:/admin/users?sort=" + sortId;
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("students", result.get("students"));
		model.addAttribute("sortId", sortId);
		model.addAttribute("positions", positions);
		model.addAttribute("positionList", Profile.POSITION_LIST);
		return "admin/users/index";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/users", params = {"pos", "cond=or", "download"})
	public ModelAndView downloadUsersIndexFilteredByPosOrPos(@RequestParam(name="sort") String sort, @RequestParam(name="pos") List<String> positions) {
		int sortId;
		try {
			sortId = Integer.parseInt(sort);
		} catch (NumberFormatException e) {
			throw new NotFound404();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> filter = Arrays.asList("役職(OR)", String.join(",", positions));
		List<Student> students = new ArrayList<Student>();
		if (!(positions.isEmpty()) && !(positions.get(0).isEmpty())) {
			students = (List<Student>) profileService.getProfilesByPosition(sortId, positions, true).get("students");
		}
		students.sort(Comparator.comparing(Student::getLast_login, Comparator.reverseOrder()));
		map.put("filter", filter);
		map.put("students", students);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
		ModelAndView mav = new ModelAndView(new ExcelBuilder(today + "_students_pos_or" + ".xlsx"), map);
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
		
//	@Mapping(value="/search/experiences", params="all")
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
	
//	@Mapping(value="/search/experiences", params="by-name")
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
	
//	@Mapping(value="/search/experiences", params="by-university")
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
	
//	@Mapping(value="/search/experiences", params="and-search-by-position")
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
	
//	@Mapping(value="/search/experiences", params="or-search-by-position")
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
	
	@GetMapping(value = "/mail", params="develop")
	public String redirectToIndex() {
		return "redirect:/admin";
	}
}
