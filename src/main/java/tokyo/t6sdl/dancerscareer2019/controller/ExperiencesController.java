package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Es;
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
		List<String> likes = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		if (likes.contains(experienceId)) {
			model.addAttribute("isLiked", true);
		} else {
			model.addAttribute("isLiked", false);
		}
		return this.display(id, model);
	}
	
	@RequestMapping(value="/{experienceId}", params="like")
	public String getLikeExperience(@PathVariable(name="experienceId") String experienceId, Model model) {
		int id = Integer.parseInt(experienceId);
		List<String> likesData = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		List<String> likes = new ArrayList<String>();
		likes.addAll(likesData);
		if (likes.contains("")) {
			likes.remove("");
		}
		if (!(likes.contains(experienceId))) {
			likes.add(experienceId);
			Collections.sort(likes);
			experienceService.updateLikes(id, true);
			profileService.updateLikes(securityService.findLoggedInEmail(), likes);
		}
		model.addAttribute("isLiked", true);
		return this.display(id, model);
	}
	
	@RequestMapping(value="/{experienceId}", params="dislike")
	public String getDislikeExperience(@PathVariable(name="experienceId") String experienceId, Model model) {
		int id = Integer.parseInt(experienceId);
		List<String> likesData = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		List<String> likes = new ArrayList<String>();
		likes.addAll(likesData);
		if (likes.size() == 1) {
			likes.add("");
		}
		if (likes.contains(experienceId)) {
			likes.remove(experienceId);
			experienceService.updateLikes(id, false);
			profileService.updateLikes(securityService.findLoggedInEmail(), likes);
		}
		model.addAttribute("isLiked", false);
		return this.display(id, model);
	}
	
	private String display(int id, Model model) {
		if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
			Experience experience = experienceService.getExperienceById(id, true, true);
			List<Es> es = new ArrayList<Es>();
			experience.getEs().forEach(e -> {
				if (!(es.isEmpty()) && es.stream().filter(s -> s.getCorp().equals(e.getCorp())).count() > 0) {
					e.setRepeated(true);
				} else {
					e.setRepeated(false);
				}
				es.add(e);
			});
			experience.setEs(es);
			model.addAttribute("experience", experience);
			return "experiences/article";
		} else if (!(securityService.findLoggedInValidEmail()) || !(profileService.isCompleteProfile(profileService.getProfileByEmail(securityService.findLoggedInEmail())))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
			Experience experience = experienceService.getExperienceById(id, true, true);
			List<Es> es = new ArrayList<Es>();
			experience.getEs().forEach(e -> {
				if (!(es.isEmpty()) && es.stream().filter(s -> s.getCorp().equals(e.getCorp())).count() > 0) {
					e.setRepeated(true);
				} else {
					e.setRepeated(false);
				}
				es.add(e);
			});
			experience.setEs(es);
			model.addAttribute("experience", experience);
			return "experiences/article";
		}
	}
}
