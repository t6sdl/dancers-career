package tokyo.t6sdl.dancerscareer2019.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tokyo.t6sdl.dancerscareer2019.model.Mail;

@Controller
@RequestMapping("/mails")
public class MailsController {
	@ModelAttribute
	public void setUp(Model model) {
		model.addAttribute("logo", Mail.CONTEXT_PATH + "/img/mails/logo.jpg");
		model.addAttribute("twitter", Mail.CONTEXT_PATH + "/img/mails/twitter.jpg");
		model.addAttribute("instagram", Mail.CONTEXT_PATH + "/img/mails/instagram.jpg");
		model.addAttribute("contact", Mail.CONTEXT_PATH + "/about/contact");
	}
	
	@RequestMapping("/welcome-to-us")
	public String getWelcomeToUs(@RequestParam("url") String url, Model model) {
		model.addAttribute("button", url);
		return "mails/welcome-to-us";
	}
	
	@RequestMapping("/verify-email")
	public String getVerifyEmail(@RequestParam("url") String url, Model model) {
		model.addAttribute("button", url);
		return "mails/verify-email";
	}
	
	@RequestMapping("/forget-pwd")
	public String getResetPWD(@RequestParam("url") String url, Model model) {
		model.addAttribute("button", url);
		return "mails/forget-pwd";
	}
	
	@RequestMapping("/reply-to-contact")
	public String getReplyToContact() {
		return "mails/reply-to-contact";
	}
}
