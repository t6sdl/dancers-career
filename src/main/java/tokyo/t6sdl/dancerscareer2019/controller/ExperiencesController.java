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
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
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
		@SuppressWarnings("unchecked")
		List<Experience> experiences = (List<Experience>) result.get("experiences");
		List<String> likes = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		if (!Objects.equals(experiences, null) && experiences.size() > 0) {
			experiences.forEach(exp -> {
				exp.setLiked(likes.contains(String.valueOf(exp.getId())));
			});
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("experiences", experiences);
		return "experiences/experiences";
	}
	
	@RequestMapping(params="by-position")
	public String getExperiencesByPosition(SearchForm form, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
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
		@SuppressWarnings("unchecked")
		List<Experience> experiences = (List<Experience>) result.get("experiences");
		List<String> likes = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		if (!Objects.equals(experiences, null) && experiences.size() > 0) {
			experiences.forEach(exp -> {
				exp.setLiked(likes.contains(String.valueOf(exp.getId())));
			});
		}
		model.addAttribute("count", result.get("count"));
		model.addAttribute("experiences", experiences);
		return "experiences/experiences";
	}
	
	@RequestMapping("/{experienceId}")
	public String getExperience(@PathVariable(name="experienceId") String experienceId, Model model) {
		int id = Integer.parseInt(experienceId);
		List<String> likes = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		return this.display(id, true, likes.contains(experienceId), model);
	}
	
	@RequestMapping(value="/{experienceId}", params="like")
	public String getLikeExperience(@PathVariable(name="experienceId") String experienceId, Model model) {
		if (Objects.equals(accountService.getAccountByEmail(securityService.findLoggedInEmail()), null)) {
			throw new NotFound404();
		}
		int id = Integer.parseInt(experienceId);
		List<String> likes = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		if (!(likes.contains(experienceId))) {
			likes.remove("");
			likes.add(experienceId);
			Collections.sort(likes, (s1, s2) -> Integer.parseInt(s1) - Integer.parseInt(s2));
			experienceService.updateLikes(id, true);
			profileService.updateLikes(securityService.findLoggedInEmail(), likes);
		}
		return this.display(id, false, true, model);
	}
	
	@RequestMapping(value="/{experienceId}", params="dislike")
	public String getDislikeExperience(@PathVariable(name="experienceId") String experienceId, Model model) {
		if (Objects.equals(accountService.getAccountByEmail(securityService.findLoggedInEmail()), null)) {
			throw new NotFound404();
		}
		int id = Integer.parseInt(experienceId);
		List<String> likes = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		if (likes.contains(experienceId)) {
			likes.remove(experienceId);
			experienceService.updateLikes(id, false);
			profileService.updateLikes(securityService.findLoggedInEmail(), likes);
		}
		return this.display(id, false, false, model);
	}
	
	private String display(int id, boolean pvCount, boolean isLiked, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		Experience experience = new Experience();
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
			model.addAttribute("isStranger", true);
			experience = experienceService.getALittleExperienceById(id);
			experience.setLiked(isLiked);
			model.addAttribute("title", experience.getUnivName() + experience.getUnivFac() + experience.getUnivDep());
			model.addAttribute("description", (experience.getEs().size() == 0 ? "" : experience.getEs().get(0).getQuestion().get(0)) + (experience.getEs().size() == 0 ? "" : experience.getEs().get(0).getAnswer().get(0)) + experience.getInterview().get(0).getQuestion() + experience.getInterview().get(0).getAnswer());
			model.addAttribute("experience", experience);
			return "experiences/aLittleArticle";
		}
		boolean perfect = profileService.isCompleteProfile(profileService.getProfileByEmail(account.getEmail()));
		if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else if (!(account.isValidEmail()) && !(perfect)) {
			model.addAttribute("header", "for-user");
			model.addAttribute("isStranger", false);
			experience = experienceService.getALittleExperienceById(id);
			experience.setLiked(isLiked);
			model.addAttribute("title", experience.getUnivName() + experience.getUnivFac() + experience.getUnivDep());
			model.addAttribute("description", (experience.getEs().size() == 0 ? "" : experience.getEs().get(0).getQuestion().get(0)) + (experience.getEs().size() == 0 ? "" : experience.getEs().get(0).getAnswer().get(0)) + experience.getInterview().get(0).getQuestion() + experience.getInterview().get(0).getAnswer());
			model.addAttribute("experience", experience);
			return "experiences/aLittleArticle";
		} else if (!(account.isValidEmail()) || !(perfect)) {
			model.addAttribute("header", "for-user");
			experience = experienceService.getExperienceById(id, true, false);
			experience.setLiked(isLiked);
			model.addAttribute("title", experience.getUnivName() + experience.getUnivFac() + experience.getUnivDep());
			model.addAttribute("otherEs", experience.getEs().size() - 1);
			model.addAttribute("validEmail", account.isValidEmail());
			model.addAttribute("experience", experience);
			return "experiences/halfOfArticle";
		} else {
			model.addAttribute("header", "for-user");
		}
		experience = experienceService.getExperienceById(id, true, pvCount);
		experience.setLiked(isLiked);
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
				e.setExpId(each.getExpId());
				e.setId(each.getId());
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
		model.addAttribute("title", experience.getUnivName() + experience.getUnivFac() + experience.getUnivDep());
		model.addAttribute("experience", experience);
		return "experiences/fullArticle";
	}
}
