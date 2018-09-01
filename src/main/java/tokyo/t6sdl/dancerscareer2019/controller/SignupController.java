package tokyo.t6sdl.dancerscareer2019.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.EmailForm;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.SignupForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.MailService;
import tokyo.t6sdl.dancerscareer2019.service.ProfileService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@Controller
@RequestMapping("/signup")
public class SignupController {
	private final AccountService accountService;
	private final SecurityService securityService;
	private final ProfileService profileService;
	private final MailService mailService;
	
	public SignupController(AccountService accountService, SecurityService securityService, ProfileService profileService, MailService mailService) {
		this.accountService = accountService;
		this.securityService = securityService;
		this.profileService = profileService;
		this.mailService = mailService;
	}
	
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
		mailService.sendMailWithUrl(form.getEmail(), MailService.SUB_VERIFY_EMAIL, MailService.CONTEXT_PATH + "/signup/verify-email?token=" + emailToken);
		securityService.autoLogin(form.getEmail(), form.getPassword());
		return "redirect:/signup/profile";
	}
	
	@GetMapping("/profile")
	public String getSignupProfile(Model model) {
		model.addAttribute("profileForm", new ProfileForm());
		return "signup/profileForm";
	}
	
	@PostMapping("/profile")
	public String postSignupProfile(@Validated ProfileForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "signup/profileForm";
		}
		String graduation = form.getGraduation_year() + "/" + form.getGraduation_month();
		Profile newProfile = new Profile();
		newProfile.setLast_name(form.getLast_name());
		newProfile.setFirst_name(form.getFirst_name());
		newProfile.setKana_last_name(form.getKana_last_name());
		newProfile.setKana_first_name(form.getKana_first_name());
		newProfile.setDate_of_birth(LocalDate.of(Integer.parseInt(form.getBirth_year()), Integer.parseInt(form.getBirth_month()), Integer.parseInt(form.getBirth_day())));
		newProfile.setSex(form.getSex());
		newProfile.setPhone_number(form.getPhone_number());
		newProfile.setMajor(form.getMajor());
		newProfile.setUniversity(form.getUniversity());
		newProfile.setFaculty(form.getFaculty());
		newProfile.setDepartment(form.getDepartment());
		newProfile.setGraduation(graduation);
		newProfile.setAcademic_degree(form.getAcademic_degree());
		newProfile.setPosition(form.getPosition());
		profileService.register(newProfile, securityService.findLoggedInEmail());
		return "redirect:/";
	}
	
	@GetMapping("/verify-email")
	public String getConfirmEmail(@RequestParam("token") String token, Model model) {
		if (accountService.isValidEmailToken(token)) {
			return "signup/verifyEmail";
		} else {
			throw new NotFound404();
		}
	}
	
	@PostMapping("/reverify-email")
	public String postReverifyEmail(@Validated EmailForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "signup/reverifyEmail";
		} else {
			return "signup/sentEmail";
		}
	}
	
	@GetMapping("/reverify-email")
	public String getReverifyEmail(Model model) {
		model.addAttribute(new EmailForm());
		return "signup/reverifyEmail";
	}
}