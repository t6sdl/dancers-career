package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.Student;
import tokyo.t6sdl.dancerscareer2019.model.form.EsForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ExperienceForm;
import tokyo.t6sdl.dancerscareer2019.model.form.InterviewForm;
import tokyo.t6sdl.dancerscareer2019.model.form.StudentForm;
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
	
	@RequestMapping(value="/students/search", params="all")
	public String getSearchStudents(Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(new StudentForm());
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
	
	@GetMapping(value="/students/search", params="by-name")
	public String getSearchStudentsByName(@RequestParam(name="kanaLastName") String kanaLastName, @RequestParam(name="kanaFirstName", required=false) String kanaFirstName, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		StudentForm form = new StudentForm();
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
	
	@GetMapping(value="/students/search", params="by-university")
	public String getSearchStudentsByUniveristy(@RequestParam(name="prefecture") String prefecture, @RequestParam(name="university", required=false) String university, @RequestParam(name="faculty", required=false) String faculty, @RequestParam(name="department", required=false) String department, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		StudentForm form = new StudentForm();
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
		if (!(department.equals("default"))) {
			profiles = profileService.getProfilesByDepartment(university, faculty, department);
		} else if (!(faculty.equals("default"))) {
			profiles = profileService.getProfilesByFaculty(university, faculty);
		} else if (!(university.equals("default"))) {
			profiles = profileService.getProfilesByUniversity(university);
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
	
	@GetMapping(value="/students/search", params="and-search-by-position")
	public String getAndSearchStudentsByPosition(@RequestParam(name="position") List<String> position, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		StudentForm form = new StudentForm();
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
	
	@GetMapping(value="/students/search", params="or-search-by-position")
	public String getOrSearchStudentsByPosition(@RequestParam(name="position") List<String> position, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		StudentForm form = new StudentForm();
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
	
	@GetMapping("/experiences/submit")
	public String getSubmitExperiences(Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		ExperienceForm experienceForm = new ExperienceForm();
		experienceForm.init();
		model.addAttribute(experienceForm);
		return "admin/experiences/submit";
	}
	
	@PostMapping("/experiences/submit")
	public String postSubmitExperiences(@Validated ExperienceForm form, BindingResult result, Model model) {
		form.setClub(this.cleanUp(form.getClub(), ""));
		form.setOffer(this.cleanUp(form.getOffer(), ""));
		form.setEs(this.cleanUp(form.getEs(), new EsForm()));
		form.setInterview(this.cleanUp(form.getInterview(), new InterviewForm()));
		if (result.hasErrors()) {
			model.addAttribute("positionList", Profile.POSITION_LIST);
			return "admin/experiences/submit";
		}
		model.addAttribute(form);
		return "admin/experiences/confirm";
	}
	
	@PostMapping(value="/experiences/complete", params="no")
	public String postNotCompleteExperiences(@Validated ExperienceForm form, BindingResult result, Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		model.addAttribute(form);
		return "admin/experiences/submit";
	}
	
	@PostMapping(value="/experiences/complete", params="yes")
	public String postCompleteExperiences(@Validated ExperienceForm form, BindingResult result, Model model) {
		Experience newExperience = experienceService.convertExperienceFormIntoExperience(form);
		experienceService.register(newExperience);
		return "admin/experiences/complete";
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
}
