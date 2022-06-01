package tokyo.t6sdl.dancerscareer.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.model.Mentor;
import tokyo.t6sdl.dancerscareer.service.AccountService;
import tokyo.t6sdl.dancerscareer.service.MentorService;
import tokyo.t6sdl.dancerscareer.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mentors")
public class MentorsController {
	private final SecurityService securityService;
	private final AccountService accountService;
	private final MentorService mentorService;

	@RequestMapping()
	public String index(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		List<Mentor> mentors = mentorService.getAll();
		model.addAttribute("mentors", mentors);
		return "mentors/index";
	}

	@RequestMapping("/{mentorId}")
	public String show(@PathVariable("mentorId") Integer mentorId, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		Mentor mentor = mentorService.find(mentorId);
		model.addAttribute("mentor", mentor);
		return "mentors/show";
	}
}
