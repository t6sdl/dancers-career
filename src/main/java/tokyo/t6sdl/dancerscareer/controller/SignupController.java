package tokyo.t6sdl.dancerscareer.controller;

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
import tokyo.t6sdl.dancerscareer.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer.io.EmailSender;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.model.Mail;
import tokyo.t6sdl.dancerscareer.model.Profile;
import tokyo.t6sdl.dancerscareer.model.form.ProfileForm;
import tokyo.t6sdl.dancerscareer.model.form.SignupForm;
import tokyo.t6sdl.dancerscareer.service.AccountService;
import tokyo.t6sdl.dancerscareer.service.ProfileService;
import tokyo.t6sdl.dancerscareer.service.SecurityService;

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
			model.addAttribute("hiddenUnivType", form.getUnivType());
			model.addAttribute("hiddenUnivName", form.getUnivName());
			model.addAttribute("hiddenUnivFac", form.getUnivFac());
			model.addAttribute("hiddenUnivDep", form.getUnivDep());
			model.addAttribute("hiddenGradType", form.getGradType());
			model.addAttribute("hiddenGradName", form.getGradName());
			model.addAttribute("hiddenGradSchool", form.getGradSchool());
			model.addAttribute("hiddenGradDiv", form.getGradDiv());
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
		return "redirect:/help/howToGetEmail";
	}
}
