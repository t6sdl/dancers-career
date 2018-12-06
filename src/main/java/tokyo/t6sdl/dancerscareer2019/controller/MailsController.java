package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.ExperienceService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mails")
public class MailsController {
	private final AccountService accountService;
	private final ExperienceService experienceService;
	
	@ModelAttribute
	public void setUp(Model model) {
		model.addAttribute("logo", Mail.CONTEXT_PATH + "/img/mails/logo.jpg");
		model.addAttribute("twitter", Mail.CONTEXT_PATH + "/img/mails/twitter.jpg");
		model.addAttribute("instagram", Mail.CONTEXT_PATH + "/img/mails/instagram.jpg");
		model.addAttribute("contact", Mail.CONTEXT_PATH + "/about/contact");
	}
	
	@RequestMapping("/welcome-to-us")
	public String getWelcomeToUs(@RequestParam("to") String to, Model model) {
		model.addAttribute("button", Mail.URI_VERIFY_EMAIL + accountService.getEmailTokenByEmail(to));
		return "mails/welcome-to-us";
	}
	
	@RequestMapping("/verify-email")
	public String getVerifyEmail(@RequestParam("to") String to, Model model) {
		model.addAttribute("button", Mail.URI_VERIFY_EMAIL + accountService.getEmailTokenByEmail(to));
		return "mails/verify-email";
	}
	
	@RequestMapping("/forget-pwd")
	public String getResetPWD(@RequestParam("to") String to, Model model) {
		model.addAttribute("button", Mail.URI_RESET_PWD + accountService.getPasswordTokenByEmail(to));
		return "mails/forget-pwd";
	}
	
	@RequestMapping("/reply-to-contact")
	public String getReplyToContact() {
		return "mails/reply-to-contact";
	}
	
	@RequestMapping("/new-es-mail")
	public String getNewEsMail(Model model) {
		Map<String, Object> results = experienceService.getExperiencesByCreatedAt();
		model.addAttribute("count", results.get("count"));
		model.addAttribute("experiences", results.get("experiences"));
		model.addAttribute("button", Mail.URI_EXPERIENCES + "?all&sort=0");
		model.addAttribute("expurl", Mail.URI_EXPERIENCES + "/");
		model.addAttribute("mailSetting", Mail.URI_MAIL_SETTING);
		return "mails/new-es-mail";
	}
}
