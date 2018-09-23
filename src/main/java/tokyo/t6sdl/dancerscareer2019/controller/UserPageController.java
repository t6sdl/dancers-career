package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.form.AccountForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.form.VerificationForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.ExperienceService;
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
	private final ExperienceService experienceService;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	private final HttpSession session;
		
	@RequestMapping()
	public String getMypage(Model model) {
		String lastName = profileService.getLastNameByEmail(securityService.findLoggedInEmail());
		List<String> likesData = profileService.getLikesByEmail(securityService.findLoggedInEmail());
		List<Experience> experiences = new ArrayList<Experience>();
		List<String> likes = new ArrayList<String>();
		likes.addAll(likesData);
		if (likes.contains("")) {
			likes.remove("");
		} else {
			likes.forEach(like -> {
				int id = Integer.parseInt(like);
				experiences.add(experienceService.getExperienceById(id, false, false));
			});
		}
		Collections.reverse(experiences);
		model.addAttribute("lastName", lastName);
		model.addAttribute("likes", likes);
		model.addAttribute("experiences", experiences);
		return "user/user";
	}
	
	@RequestMapping("/error")
	public String getError() {
		return "user/error";
	}
	
	@RequestMapping("/account")
	public String getAccountInfo(Model model) {
		String loggedInEmail = securityService.findLoggedInEmail();
		model.addAttribute("email", loggedInEmail);
		model.addAttribute("validEmail", securityService.findLoggedInValidEmail());
		return "user/account/account";
	}
	
	@RequestMapping("/account/help")
	public String getHelp() {
		return "user/account/help";
	}
	
	@RequestMapping("/account/verify")
	public String getVerificationToChangeAccount(Model model) {
		model.addAttribute("cod", "change");
		model.addAttribute(new VerificationForm());
		return "user/account/verification";
	}
	
	@GetMapping("/account/delete")
	public String getVerificationToDeleteAccount(Model model) {
		model.addAttribute("cod", "delete");
		model.addAttribute(new VerificationForm());
		return "user/account/verification";
	}
	
	@PostMapping("/account/delete")
	public String postVerificationToDeleteAccount(VerificationForm form, Model model) {
		if (passwordEncoder.matches(form.getPassword(), securityService.findLoggedInPassword())) {
			String loggedInEmail = securityService.findLoggedInEmail();
			profileService.delete(loggedInEmail);
			accountService.delete(loggedInEmail);
			return "redirect:/logout";
		} else {
			return "redirect:/user/account/delete?error";
		}
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
		Mail mail = new Mail(form.getEmail(), Mail.SUB_VERIFY_EMAIL);
		mail.setUrl(Mail.URI_VERIFY_EMAIL + emailToken);
		mailService.sendMail(mail);
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
		if (Objects.equals(profile, null)) {
			profile = new Profile();
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
			model.addAttribute("positionList", Profile.POSITION_LIST);
			Profile profile = profileService.getProfileByEmail(securityService.findLoggedInEmail());
			ProfileForm form;
			if (Objects.equals(profile, null)) {
				form = new ProfileForm();
			} else {
				form = profileService.convertProfileIntoProfileForm(profile);
			}
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
			model.addAttribute("positionList", Profile.POSITION_LIST);
			return "user/profile/changeProfile";
		} else {
			String loggedInEmail = securityService.findLoggedInEmail();
			Profile updatedProfile = profileService.convertProfileFormIntoProfile(form);
			if (Objects.equals(profileService.getProfileByEmail(loggedInEmail), null)) {
				profileService.register(updatedProfile, loggedInEmail);
			} else {
				profileService.update(updatedProfile, loggedInEmail);
			}
			return "redirect:/user/profile";
		}
	}
}
