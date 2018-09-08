package tokyo.t6sdl.dancerscareer2019.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/experiences")
public class ExperiencesController {
	private final SecurityService securityService;
	private final ProfileService profileService;
	
	@RequestMapping()
	public String getExperiences(Model model) {
		if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
			return "experiences/experiences";
		} else if (!(securityService.findLoggedInValidEmail()) || profileService.isCompleteProfile(profileService.getProfileByEmail(securityService.findLoggedInEmail()))) {
			return "experiences/error";
		} else {
			model.addAttribute("header", "for-user");
			return "experiences/experiences";
		}
	}
}
