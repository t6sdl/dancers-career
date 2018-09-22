package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.form.SearchForm;
import tokyo.t6sdl.dancerscareer2019.service.ExperienceService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/experiences")
public class ExperiencesController {
	private final SecurityService securityService;
	private final ProfileService profileService;
	private final ExperienceService experienceService;
	
	@RequestMapping(params="all")
	public String getExperiences(Model model) {
		if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
			model.addAttribute("posistionList", Profile.POSITION_LIST);
			model.addAttribute(new SearchForm());
			List<Experience> experiences = experienceService.getExperiences();
			model.addAttribute("experiences", experiences);
			return "experiences/experiences";
		} else if (!(securityService.findLoggedInValidEmail()) || !(profileService.isCompleteProfile(profileService.getProfileByEmail(securityService.findLoggedInEmail())))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
			model.addAttribute("positionList", Profile.POSITION_LIST);
			model.addAttribute(new SearchForm());
			List<Experience> experiences = experienceService.getExperiences();
			model.addAttribute("experiences", experiences);
			return "experiences/experiences";
		}
	}
	
	@RequestMapping(params="by-position")
	public String getExperiencesByPosition(@RequestParam(name="position") List<String> position, SearchForm form, Model model) {
		if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
			model.addAttribute("posistionList", Profile.POSITION_LIST);
			model.addAttribute(form);
			List<Experience> experiences = experienceService.getExperiencesByPosition(position, "OR");
			model.addAttribute("experiences", experiences);
			return "experiences/experiences";
		} else if (!(securityService.findLoggedInValidEmail()) || !(profileService.isCompleteProfile(profileService.getProfileByEmail(securityService.findLoggedInEmail())))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
			model.addAttribute("positionList", Profile.POSITION_LIST);
			model.addAttribute(form);
			List<Experience> experiences = experienceService.getExperiencesByPosition(position, "OR");
			model.addAttribute("experiences", experiences);
			return "experiences/experiences";
		}
	}
	
	@RequestMapping("/{experienceId}")
	public String getExperience(@PathVariable(name="experienceId") String experienceId, Model model) {
		int id = Integer.parseInt(experienceId);
		if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
			Experience experience = experienceService.getExperienceById(id, true);
			model.addAttribute("experience", experience);
			return "experiences/article";
		} else if (!(securityService.findLoggedInValidEmail()) || !(profileService.isCompleteProfile(profileService.getProfileByEmail(securityService.findLoggedInEmail())))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
			Experience experience = experienceService.getExperienceById(id, true);
			model.addAttribute("experience", experience);
			return "experiences/article";
		}
	}
}
