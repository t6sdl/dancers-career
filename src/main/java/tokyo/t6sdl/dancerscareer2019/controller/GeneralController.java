package tokyo.t6sdl.dancerscareer2019.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.form.ContactForm;
import tokyo.t6sdl.dancerscareer2019.service.MailService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
public class GeneralController {
	private final SecurityService securityService;
	private final MailService mailService;
	
	@RequestMapping("")
	public String isndex(Model model) {
		if (securityService.findLoggedInAuthority()) {
			return "redirect:/admin";
		}
		return "index/index";
	}
	
	@RequestMapping("/about/privacy")
	public String getPrivacyPolicy(Model model) {
		if (securityService.findLoggedInEmail().equals("")) {
			model.addAttribute("header", "for-stranger");
		} else if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		return "about/privacyPolicy";
	}
	
	@RequestMapping("/about/terms")
	public String getTermsOfUse(Model model) {
		if (securityService.findLoggedInEmail().equals("")) {
			model.addAttribute("header", "for-stranger");
		} else if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		return "about/terms";
	}
	
	@GetMapping("/about/contact")
	public String getContact(Model model) {
		String loggedInEmail = securityService.findLoggedInEmail();
		ContactForm contactForm = new ContactForm();
		contactForm.setFrom(loggedInEmail);
		if (securityService.findLoggedInEmail().equals("")) {
			model.addAttribute("header", "for-stranger");
		} else if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		model.addAttribute(contactForm);
		boolean isLoggedIn = !(loggedInEmail.equals(""));
		model.addAttribute("isLoggedIn", isLoggedIn);
		return "about/contact/contact";
	}
	
	@PostMapping("/about/contact")
	public String postContact(ContactForm contactForm, Model model) {
		Mail mail = new Mail(Mail.TO_SUPPORT, Mail.SUB_CONTACT);
		mail.setContent(contactForm.getContent());
		mailService.receiveMail(mail);
		System.out.println(contactForm.getFrom());
		Mail reply = new Mail(contactForm.getFrom(), Mail.SUB_REPLY_TO_CONTACT);
		mailService.sendMail(reply);
		if (securityService.findLoggedInEmail().equals("")) {
			model.addAttribute("header", "for-stranger");
		} else if (securityService.findLoggedInAuthority()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		model.addAttribute(contactForm);
		return "about/contact/sentContact";
	}
}