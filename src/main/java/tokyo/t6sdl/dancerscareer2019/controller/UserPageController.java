package tokyo.t6sdl.dancerscareer2019.controller;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.form.AccountForm;
import tokyo.t6sdl.dancerscareer2019.model.form.EmailForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.form.VerificationForm;
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
	private final HttpSession session;
		
	@RequestMapping()
	public String getMypage() {
		return "user/user";
	}
	
	@RequestMapping("/error")
	public String getError() {
		return "user/error";
	}
	
	@RequestMapping("/personality")
	public String getPesonality() {
		return "user/personality/result";
	}
	
	@RequestMapping("/account")
	public String getAccountInfo(Model model) {
		String loggedInEmail = securityService.findLoggedInEmail();
		model.addAttribute("email", loggedInEmail);
		boolean validEmail = securityService.findLoggedInValidEmail();
		if (validEmail == false) {
			EmailForm form = new EmailForm();
			form.setEmail(loggedInEmail);
			model.addAttribute(form);
		}
		model.addAttribute("validEmail", validEmail);
		return "user/account/account";
	}
	
	@RequestMapping("/account/help")
	public String getHelp() {
		return "user/account/help";
	}
	
	@RequestMapping("/account/verify")
	public String getVerificationToChangeAccount(Model model) {
		model.addAttribute(new VerificationForm());
		return "user/account/verification";
	}
	
	@PostMapping("/account/change")
	public String postVerificationToChangeAccount(VerificationForm form, Model model) {
		if (passwordEncoder.matches(form.getPassword(), securityService.findLoggedInPassword())) {
			session.setAttribute("rawPassword", form.getPassword());
			model.addAttribute(new AccountForm());
			model.addAttribute("emailError", false);
			model.addAttribute("passwordError", false);
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
		accountService.changeValidEmail(form.getEmail(), false);
		mailService.sendMailWithUrl(form.getEmail(), Mail.SUB_VERIFY_EMAIL, Mail.CONTEXT_PATH + "/signup/verify-email?token=" + emailToken);
		String loggedInRawPassword = session.getAttribute("rawPassword").toString();
		securityService.autoLogin(form.getEmail(), loggedInRawPassword);
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
		session.setAttribute("rawPassword", form.getPassword());
		securityService.autoLogin(loggedInEmail, form.getPassword());
		return "redirect:/user/account";
	}
	
	@RequestMapping("/profile")
	public String getProfileInfo(Model model) {
		String loggedInEmail = securityService.findLoggedInEmail();
		Profile profile = profileService.getProfileByEmail(loggedInEmail);
		if (profile.getEmail() == null) {
			for (Field field: profile.getClass().getDeclaredFields()) {
				try {
					field.setAccessible(true);
					if (field.getType() == String.class) {
						field.set(profile, "");
					} else if (field.getType() == List.class) {
						String[] arr = {""};
						List<String> pos = Arrays.asList(arr);
						field.set(profile, pos);
					}
				} catch (IllegalAccessException e) {
				}
			}
		} else {
			profile.convertForDisplay();
		}
		model.addAttribute("profile", profile);
		return "user/profile/profile";
	}
	
	@RequestMapping("/profile/verify")
	public String getVerificationToChangeProfile(Model model) {
		model.addAttribute(new VerificationForm());
		return "user/profile/verification";
	}
	
	@PostMapping("/profile/change")
	public String postVerificationToChangeProfile(VerificationForm verificationForm, Model model) {
		if (passwordEncoder.matches(verificationForm.getPassword(), securityService.findLoggedInPassword())) {
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
			if (profileService.getProfileByEmail(loggedInEmail).getEmail() == null) {
				profileService.register(updatedProfile, loggedInEmail);
			}else {
				profileService.update(updatedProfile, loggedInEmail);
			}
			return "redirect:/user/profile";
		}
	}
}
