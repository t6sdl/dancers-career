package tokyo.t6sdl.dancerscareer2019.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mails")
public class MailsController {
	@RequestMapping("/welcome-to-us")
	public String getWelcomeToUs(@RequestParam("url") String url, Model model) {
		model.addAttribute("url", url);
		return "mails/welcome-to-us";
	}
	
	@RequestMapping("/verify-email")
	public String getVerifyEmail(@RequestParam("url") String url, Model model) {
		model.addAttribute("url", url);
		return "mails/verify-email";
	}
	
	@RequestMapping("/forget-pwd")
	public String getResetPWD(@RequestParam("url") String url, Model model) {
		model.addAttribute("url", url);
		return "mails/forget-pwd";
	}
	
	@RequestMapping("/reply-to-contact")
	public String getReplyToContact() {
		return "mails/reply-to-contact";
	}
}
