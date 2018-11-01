package tokyo.t6sdl.dancerscareer2019.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.io.EmailSender;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.form.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.form.SignupForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signup")
public class SignupController {
	private final AccountService accountService;
	private final SecurityService securityService;
	private final ProfileService profileService;
	private final EmailSender emailSender;
	private final HttpSession session;
	
	@GetMapping
	public String getSignup(Model model) {
		model.addAttribute(new SignupForm());
		return "signup/signupForm";
	}
	
	@PostMapping
	public String postSignup(@Validated SignupForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "signup/signupForm";
		}
		Account newAccount = new Account();
		newAccount.setEmail(form.getNewEmail());
		accountService.create(newAccount, form.getNewPassword());
		securityService.autoLogin(form.getNewEmail(), form.getNewPassword());
		Mail mail = new Mail(form.getNewEmail(), Mail.SUB_WELCOME_TO_US);
		try {
			emailSender.sendMailWithToken(mail);
		} catch (Exception e) {
			accountService.delete(mail.getTo());
			return "redirect:/signup?error";
		}
		session.setAttribute("rawPassword", form.getNewPassword());
		return "redirect:/signup/profile";
	}
	
	@GetMapping("/profile")
	public String getSignupProfile(Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		ProfileForm form = new ProfileForm();
		form.setApplyLineNotify(true);
		model.addAttribute(form);
		return "signup/profileForm";
	}
	
	@PostMapping("/profile")
	public String postSignupProfile(@Validated ProfileForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("hiddenUnivName", form.getUnivName());
			model.addAttribute("hiddenFac", form.getFaculty());
			model.addAttribute("hiddenDep", form.getDepartment());
			model.addAttribute("hiddenGradSchoolName", form.getGradSchoolName());
			model.addAttribute("hiddenGradSchoolOf", form.getGradSchoolOf());
			model.addAttribute("hiddenProgramIn", form.getProgramIn());
			model.addAttribute("positionList", Profile.POSITION_LIST);
			return "signup/profileForm";
		}
		Profile newProfile = profileService.convertProfileFormIntoProfile(form);
		profileService.register(newProfile, securityService.findLoggedInEmail());
		if (form.isApplyLineNotify()) {
			return "redirect:/line-notify/apply?from=profile";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping("/verify-email")
	public String getVerifyEmail(@RequestParam("token") String token, Model model) {
		if (accountService.isValidEmailToken(token)) {
			return "signup/verifyEmail";
		} else {
			throw new NotFound404();
		}
	}
	
	@RequestMapping("/reverify-email")
	public String getReverifyEmail() {
		String loggedInEmail = securityService.findLoggedInEmail();
		Mail mail = new Mail(loggedInEmail, Mail.SUB_VERIFY_EMAIL);
		try {
			emailSender.sendMailWithToken(mail);
		} catch (Exception e) {
			return "redirect:/user/error";
		}
		return "redirect:/user/account/help";
	}
}