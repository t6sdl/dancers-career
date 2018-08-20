package tokyo.t6sdl.dancerscareer2019.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.SignupForm;
import tokyo.t6sdl.dancerscareer2019.repository.ProfileRepository;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@Controller
@RequestMapping("/signup")
public class SignupController {
	private final AccountService accountService;
	private final SecurityService securityService;
	private final ProfileRepository profileRepository;
	
	public SignupController(AccountService accountService, SecurityService securityService, ProfileRepository profileRepository) {
		this.accountService = accountService;
		this.securityService = securityService;
		this.profileRepository = profileRepository;
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
		accountService.sendMail(form.getEmail(), "Test", "<!DOCTYPE html><html><body><p>test mail</p></body></html>");
		accountService.create(newAccount, form.getPassword());
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
		Date date_of_birth = stringToDate("yyyy/MM/dd", form.getBirth_year() + "/" + form.getBirth_month() + "/" + form.getBirth_day());
		String graduation = form.getGraduation_year() + "/" + form.getGraduation_month();
		Profile newProfile = new Profile();
		newProfile.setEmail(securityService.findLoggedInEmail());
		newProfile.setLast_name(form.getLast_name());
		newProfile.setFirst_name(form.getFirst_name());
		newProfile.setKana_last_name(form.getKana_last_name());
		newProfile.setKana_first_name(form.getKana_first_name());
		newProfile.setDate_of_birth(date_of_birth);
		newProfile.setSex(form.getSex());
		newProfile.setPhone_number(form.getPhone_number());
		newProfile.setMajor(form.getMajor());
		newProfile.setUniversity(form.getUniversity());
		newProfile.setFaculty(form.getFaculty());
		newProfile.setDepartment(form.getDepartment());
		newProfile.setGraduation(graduation);
		newProfile.setAcademic_degree(form.getAcademic_degree());
		newProfile.setPosition(form.getPosition());
		profileRepository.insert(newProfile);
		return "redirect:/";
	}
	
	public Date stringToDate(String format, String str) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}