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
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.form.EmailForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.form.SignupForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.MailService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signup")
public class SignupController {
	private final AccountService accountService;
	private final SecurityService securityService;
	private final ProfileService profileService;
	private final MailService mailService;
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
		newAccount.setEmail(form.getEmail());
		accountService.create(newAccount, form.getPassword());
		String emailToken = accountService.createEmailToken(form.getEmail());
		if (emailToken == "") {
			accountService.delete(form.getEmail());
			return "redirect:/signup?error";
		}
		mailService.sendMailWithUrl(form.getEmail(), Mail.SUB_VERIFY_EMAIL, Mail.CONTEXT_PATH + "/signup/verify-email?token=" + emailToken);
		session.setAttribute("rawPassword", form.getPassword());
		securityService.autoLogin(form.getEmail(), form.getPassword());
		return "redirect:/signup/profile";
	}
	
	@GetMapping("/profile")
	public String getSignupProfile(Model model) {
		model.addAttribute(new ProfileForm());
		return "signup/profileForm";
	}
	
	@PostMapping("/profile")
	public String postSignupProfile(@Validated ProfileForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("hiddenUniv", form.getUniversity());
			model.addAttribute("hiddenFac", form.getFaculty());
			model.addAttribute("hiddenDep", form.getDepartment());
			return "signup/profileForm";
		}
		Profile newProfile = profileService.convertProfileFormIntoProfile(form);
		profileService.register(newProfile, securityService.findLoggedInEmail());
		return "redirect:/";
	}
	
	@GetMapping("/verify-email")
	public String getVerifyEmail(@RequestParam("token") String token, Model model) {
		if (accountService.isValidEmailToken(token)) {
			String loggedInEmail = securityService.findLoggedInEmail();
			String loggedInRawPassword = session.getAttribute("rawPassword").toString();
			securityService.autoLogin(loggedInEmail, loggedInRawPassword);
			session.invalidate();
			return "signup/verifyEmail";
		} else {
			throw new NotFound404();
		}
	}
	
	@PostMapping("/verify-email")
	public String postVerifyEmail(@Validated EmailForm form, BindingResult result) {
		String emailToken = accountService.createEmailToken(form.getEmail());
		if (emailToken == "") {
			return "redirect:/user/account?error";
		}
		mailService.sendMailWithUrl(form.getEmail(), Mail.SUB_VERIFY_EMAIL, Mail.CONTEXT_PATH + "/signup/verify-email?token=" + emailToken);
		return "redirect:/user/account?success";
	}
}