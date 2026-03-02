package tokyo.t6sdl.dancerscareer.controller;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.io.EmailSender;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.model.Mail;
import tokyo.t6sdl.dancerscareer.model.form.ContactForm;
import tokyo.t6sdl.dancerscareer.service.AccountService;
import tokyo.t6sdl.dancerscareer.service.SecurityService;

@RequiredArgsConstructor
@Controller
public class GeneralController {
	private final SecurityService securityService;
	private final EmailSender emailSender;
	private final AccountService accountService;

	@RequestMapping("")
	public String index(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			return "redirect:/admin";
		} else {
			model.addAttribute("header", "for-user");
		}
		return "index/index";
	}

	@RequestMapping("/about/terms-of-use")
	public String getPrivacyPolicy(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		return "about/termsOfUse";
	}

	@GetMapping("/about/contact")
	public String getContact(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		setHeader(account, model);
		if (!Objects.equals(account, null) && account.isAdmin()) {
			return "redirect:/admin";
		}
		if (Objects.equals(account, null)) {
			model.addAttribute(new ContactForm());
			model.addAttribute("isLoggedIn", false);
		} else {
			model.addAttribute(new ContactForm(account.getEmail()));
			model.addAttribute("isLoggedIn", true);
		}
		return "about/contact/contact";
	}

	@PostMapping("/about/contact")
	public String postContact(@Validated ContactForm contactForm, BindingResult result, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		setHeader(account, model);
		if (result.hasErrors()) {
			return "about/contact/contact";
		}
		Mail reply = new Mail(contactForm.getFrom(), Mail.SUB_REPLY_TO_CONTACT);
		Mail ask = new Mail(Mail.TO_SUPPORT, Mail.SUB_CONTACT);
		ask.setContent("[Email: " + contactForm.getFrom() + "]\n" + contactForm.getContent());
		emailSender.sendContactForm(reply, ask);
		model.addAttribute(contactForm);
		return "about/contact/sentContact";
	}

	@RequestMapping("/help/get-email")
	public String getHowToGetEmail() {
		return "help/howToGetEmail";
	}

	private void setHeader(Account account, Model model) {
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
	}
}
