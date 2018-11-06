package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.form.SearchForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.ExperienceService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/experiences")
public class ExperiencesController {
	private final AccountService accountService;
	private final SecurityService securityService;
	private final ProfileService profileService;
	private final ExperienceService experienceService;
	
	@RequestMapping(params="all")
	public String getExperiences(@RequestParam(name="sort") String sort, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else if (!(account.isValid_email()) || !(profileService.isCompleteProfile(profileService.getProfileByEmail(account.getEmail())))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
		}
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
		return "experiences/experiences";
	}
	
	@RequestMapping(params="by-position")
	public String getExperiencesByPosition(SearchForm form, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else if (!(account.isValid_email()) || !(profileService.isCompleteProfile(profileService.getProfileByEmail(securityService.findLoggedInEmail())))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
		}
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
		return "experiences/experiences";
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
		return this.display(id, true, model);
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
		return this.display(id, false, model);
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
		return this.display(id, false, model);
	}
	
	private String display(int id, boolean pvCount, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else if (!(account.isValid_email()) || !(profileService.isCompleteProfile(profileService.getProfileByEmail(securityService.findLoggedInEmail())))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
		}
		Experience experience = experienceService.getExperienceById(id, true, pvCount);
		List<Es> es = new ArrayList<Es>();
		Iterator<Es> iterator = experience.getEs().iterator();
		String corp = null;
		while (iterator.hasNext()) {
			Es each = iterator.next();
			if (!(each.getCorp().isEmpty()) && Objects.equals(corp, each.getCorp())) {
				es.get(es.size() - 1).getQuestion().add(each.getQuestion().get(0));
				es.get(es.size() - 1).getAnswer().add(each.getAnswer().get(0));
				es.get(es.size() - 1).getAdvice().add(each.getAdvice().get(0));
			} else {
				Es e = new Es();
				e.setExperience_id(each.getExperience_id());
				e.setEs_id(each.getEs_id());
				e.setCorp(each.getCorp());
				e.setResult(each.getResult());
				e.getQuestion().add(each.getQuestion().get(0));
				e.getAnswer().add(each.getAnswer().get(0));
				e.getAdvice().add(each.getAdvice().get(0));
				es.add(e);
			}
			corp = each.getCorp();
			iterator.remove();
		}
		experience.setEs(es);
		model.addAttribute("experience", experience);
		return "experiences/article";
	}
}
