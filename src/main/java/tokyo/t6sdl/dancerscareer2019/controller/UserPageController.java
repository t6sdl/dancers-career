package tokyo.t6sdl.dancerscareer2019.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserPageController {
	private final SecurityService securityService;
	private final AccountService accountService;
	private final ProfileService profileService;
	
	@GetMapping("")
	public String getMypage() {
		return "user/user";
	}
	
	@GetMapping("/personality")
	public String getPesonality() {
		return "user/personality/result";
	}
	
	@GetMapping("/account")
	public String getAccountInfo(Model model) {
		String loggedInEmail = securityService.findLoggedInEmail();
		model.addAttribute("email", loggedInEmail);
		return "user/account/account";
	}
	
	@GetMapping("/profile")
	public String getProfileInfo(Model model) {
		String loggedInEmail = securityService.findLoggedInEmail();
		Profile profile = profileService.getProfileByEmail(loggedInEmail);
		StringBuffer graduation = new StringBuffer();
		String[] split = profile.getGraduation().split("/");
		if (split[1].charAt(0) == '0') {
			split[1] = String.valueOf(split[1].charAt(1));
		}
		profile.setGraduation(graduation.append(split[0]).append("年").append(split[1]).append("月").toString());
		model.addAttribute("profile", profile);
		return "user/profile/profile";
	}
}
