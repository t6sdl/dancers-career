package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.io.LineNotifyManager;
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.io.EmailSender;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.form.MailMagazineForm;
import tokyo.t6sdl.dancerscareer2019.model.form.NewEmailForm;
import tokyo.t6sdl.dancerscareer2019.model.form.NewPasswordForm;
import tokyo.t6sdl.dancerscareer2019.model.form.ProfileForm;
import tokyo.t6sdl.dancerscareer2019.model.form.VerificationForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.ExperienceService;
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
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;
	private final LineNotifyManager lineNotify;
	
	@RequestMapping()
	public String getMypage(Model model) {
		String familyName = profileService.getFamilyNameByEmail(securityService.findLoggedInEmail());
		model.addAttribute("familyName", familyName);
		return "user/user";
	}
		
	@RequestMapping("/likes")
	public String getMyLikes(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		Profile profile = profileService.getProfileByEmail(account.getEmail());
		List<Experience> experiences = new ArrayList<Experience>();
		List<String> likes = Objects.equals(profile, null) ? new ArrayList<String>() : profile.getLikes();
		if (!(likes.remove(""))) {
			likes.forEach(like -> {
				int id = Integer.parseInt(like);
				experiences.add(experienceService.getExperienceById(id, false, false));
			});
			Collections.reverse(experiences);
		}
		model.addAttribute("familyName", Objects.equals(profile, null) ? null : profile.getFamilyName());
		model.addAttribute("validEmail", account.isValid_email());
		model.addAttribute("perfect", profileService.isCompleteProfile(profile));
		model.addAttribute("likes", likes.size());
		model.addAttribute("experiences", experiences);
		return "user/myLikes";
	}
	
	@RequestMapping("/error")
	public String getError() {
		return "user/error";
	}
	
	@RequestMapping("/account")
	public String getAccountInfo(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		String accessToken = accountService.getLineAccessTokenByEmail(account.getEmail());
		if (!(Objects.equals(accessToken, null))) {
			int tokenStatus = lineNotify.getTokenStatus(accessToken);
			if (tokenStatus == 200) {
				model.addAttribute("isConnected", true);
			} else {
				model.addAttribute("isConnected", false);
			}
		} else {
			model.addAttribute("isConnected", false);
		}
		model.addAttribute("email", account.getEmail());
		model.addAttribute("validEmail", account.isValid_email());
		return "user/account/account";
	}
	
	@GetMapping("/account/delete")
	public String getVerificationToDeleteAccount(Model model) {
		model.addAttribute(new VerificationForm());
		return "user/account/delete";
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
	
	@GetMapping("/account/change/{what}")
	public String getChangeAccount(@PathVariable(name="what") String what, Model model) {
		if (what.equals("email")) {
			model.addAttribute(new NewEmailForm());
			return "user/account/changeEmail";
		} else if (what.equals("password")) {
			model.addAttribute(new NewPasswordForm());
			return "user/account/changePassword";
		} else {
			throw new NotFound404();
		}
	}

	@PostMapping(value="/account/change/email")
	public String postChangeEmail(@Validated NewEmailForm form, BindingResult result, Model model) {
		if (passwordEncoder.matches(form.getCurrentPassword(), securityService.findLoggedInPassword())) {
			if (result.hasErrors()) {
				return "user/account/changeEmail";
			}
			String loggedInEmail = securityService.findLoggedInEmail();
			accountService.changeEmail(loggedInEmail, form.getNewEmail());
			Mail mail = new Mail(form.getNewEmail(), Mail.SUB_VERIFY_EMAIL);
			try {
				emailSender.sendMailWithToken(mail);
			} catch (Exception e) {
				accountService.changeEmail(form.getNewEmail(), loggedInEmail);
				return "redirect:/user/error";
			}
			accountService.changeValidEmail(form.getNewEmail(), false);
			securityService.autoLogin(form.getNewEmail(), form.getCurrentPassword());
			return "redirect:/user/account?changedemail";
		} else {
			return "redirect:/user/account/change/email?error";
		}
	}
	
	@PostMapping(value="/account/change/password")
	public String postChangePassword(@Validated NewPasswordForm form, BindingResult result, Model model) {
		if (passwordEncoder.matches(form.getCurrentPassword(), securityService.findLoggedInPassword())) {
			if (result.hasErrors() || result.hasGlobalErrors()) {
				return "user/account/changePassword";
			}
			String loggedInEmail = securityService.findLoggedInEmail();
			accountService.changePassword(loggedInEmail, form.getNewPassword());
			securityService.autoLogin(loggedInEmail, form.getNewPassword());
			return "redirect:/user/account?changedpassword";
		} else {
			return "redirect:/user/account/change/password?error";
		}
	}
	
	
	@GetMapping("/account/change/mail-setting")
	public String getMailSetting(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			return "redirect:/signin?from=mail-setting";
		} else if (account.isAdmin()) {
			return "redirect:/admin";
		} else {
			MailMagazineForm form = new MailMagazineForm();
			form.setNewEs(account.isNew_es_mail());
			model.addAttribute(form);
			return "user/account/mailSetting";
		}
	}
	
	@PostMapping("/account/change/mail-setting")
	public String postMailSetting(MailMagazineForm form, Model model) {
		accountService.changeNewEsMail(securityService.findLoggedInEmail(), form.isNewEs());
		return "redirect:/user/account?changedsetting";
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
	
	@GetMapping("/profile/change")
	public String getChangeProfile(Model model) {
		model.addAttribute("positionList", Profile.POSITION_LIST);
		Profile profile = profileService.getProfileByEmail(securityService.findLoggedInEmail());
		ProfileForm form;
		if (Objects.equals(profile, null)) {
			form = new ProfileForm();
		} else {
			form = profileService.convertProfileIntoProfileForm(profile);
		}
		model.addAttribute(form);
		model.addAttribute("hiddenUnivName", form.getUnivName());
		model.addAttribute("hiddenUnivFac", form.getUnivFac());
		model.addAttribute("hiddenUnivDep", form.getUnivDep());
		model.addAttribute("hiddenGradName", form.getGradName());
		model.addAttribute("hiddenGradSchool", form.getGradSchool());
		model.addAttribute("hiddenGradDiv", form.getGradDiv());
		return "user/profile/changeProfile";
	}
	
	@PostMapping("/profile/change")
	public String postChangeProfile(@Validated ProfileForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("hiddenUnivName", form.getUnivName());
			model.addAttribute("hiddenUnivFac", form.getUnivFac());
			model.addAttribute("hiddenUnivDep", form.getUnivDep());
			model.addAttribute("hiddenGradName", form.getGradName());
			model.addAttribute("hiddenGradSchool", form.getGradSchool());
			model.addAttribute("hiddenGradDiv", form.getGradDiv());
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
			return "redirect:/user/profile?modified";
		}
	}
}
