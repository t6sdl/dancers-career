package tokyo.t6sdl.dancerscareer2019.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.AccountForm;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.Verification;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.MailService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserPageController {
	private final SecurityService securityService;
	private final AccountService accountService;
	private final ProfileService profileService;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	
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
	
	@GetMapping("/account/verify")
	public String getVerificationToChangeAccount(Model model) {
		model.addAttribute(new Verification());
		return "user/account/verification";
	}
	
	@PostMapping("/account/change")
	public String postVerificationToChangeAccount(Verification form, Model model) {
		if (passwordEncoder.matches(form.getPassword(), securityService.findLoggedInPassword())) {
			model.addAttribute(new AccountForm());
			model.addAttribute("emailError", false);
			model.addAttribute("passwordError", false);
			model.addAttribute("hiddenPassword", form.getPassword());
			return "user/account/changeAccount";
		} else {
			return "redirect:/user/account/verify?error";
		}
	}
	
	@GetMapping("/account/change")
	public String getChangeAccount() {
		return "redirect:/user/account/verify";
	}
	
	@PostMapping(value="/account/changed", params="changeEmail")
	public String postChangeEmail(@Validated AccountForm form, BindingResult result, Model model) {
		if (result.hasFieldErrors("email")) {
			model.addAttribute("emailError", true);
			model.addAttribute("passwordError", false);
			return "user/account/changeAccount";
		}
		String loggedInEmail = securityService.findLoggedInEmail();
		accountService.changeEmail(loggedInEmail, form.getEmail());
		String emailToken = accountService.createEmailToken(form.getEmail());
		if (emailToken == "") {
			accountService.changeEmail(form.getEmail(), loggedInEmail);
			return "redirect:/user/error";
		}
		mailService.sendMailWithUrl(form.getEmail(), MailService.SUB_VERIFY_EMAIL, MailService.CONTEXT_PATH + "/signup/verify-email?token=" + emailToken);
		securityService.autoLogin(form.getEmail(), form.getHiddenPassword());
		return "redirect:/user/account";
	}
	
	@PostMapping(value="/account/changed", params="changePassword")
	public String postChangePassword(@Validated AccountForm form, BindingResult result, Model model) {
		if (result.hasFieldErrors("password") || result.hasGlobalErrors()) {
			model.addAttribute("emailError", false);
			model.addAttribute("passwordError", true);
			return "user/account/changeAccount";
		}
		String loggedInEmail = securityService.findLoggedInEmail();
		accountService.changePassword(loggedInEmail, form.getPassword());
		securityService.autoLogin(loggedInEmail, form.getPassword());
		return "redirect:/user/account";
	}
	
	@GetMapping("/profile")
	public String getProfileInfo(Model model) {
		String loggedInEmail = securityService.findLoggedInEmail();
		Profile profile = profileService.getProfileByEmail(loggedInEmail);
		profile.convertForDisplay();
		model.addAttribute("profile", profile);
		return "user/profile/profile";
	}
	
	@GetMapping("/profile/verify")
	public String getVerificationToChangeProfile(Model model) {
		model.addAttribute(new Verification());
		return "user/profile/verification";
	}
	
	@PostMapping("/profile/change")
	public String postVerificationToChangeProfile(Verification verification, Model model) {
		if (passwordEncoder.matches(verification.getPassword(), securityService.findLoggedInPassword())) {
			ProfileForm form = profileService.convertProfileIntoProfileForm(profileService.getProfileByEmail(securityService.findLoggedInEmail()));
			model.addAttribute(form);
			model.addAttribute("hiddenUniv", form.getUniversity());
			model.addAttribute("hiddenFac", form.getFaculty());
			model.addAttribute("hiddenDep", form.getDepartment());
			return "user/profile/changeProfile";
		} else {
			return "redirect:/user/profile/verify?error";
		}
	}
	
	@GetMapping("/profile/change")
	public String getChangeProfile() {
		return "redirect:/user/profile/verify";
	}
	
	@PostMapping("/profile/changed")
	public String postChangedProfile(@Validated ProfileForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("hiddenUniv", form.getUniversity());
			model.addAttribute("hiddenFac", form.getFaculty());
			model.addAttribute("hiddenDep", form.getDepartment());
			return "user/profile/changeProfile";
		} else {
			String loggedInEmail = securityService.findLoggedInEmail();
			Profile updatedProfile = profileService.convertProfileFormIntoProfile(form);
			profileService.update(updatedProfile, loggedInEmail);
			return "redirect:/user/profile";
		}
	}
}
