package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.io.LineNotifyManager;
import tokyo.t6sdl.dancerscareer2019.io.EmailSender;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.form.ContactForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
public class GeneralController {
	private final SecurityService securityService;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;
	private final LineNotifyManager lineNotify;
	private final String CONTEXT_PATH = Mail.CONTEXT_PATH;

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
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
			model.addAttribute(new ContactForm());
			model.addAttribute("isLoggedIn", false);
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
			return "redirect:/admin";
		} else {
			model.addAttribute("header", "for-user");
			model.addAttribute(new ContactForm(account.getEmail()));
			model.addAttribute("isLoggedIn", true);
		}
		return "about/contact/contact";
	}
	
	@PostMapping("/about/contact")
	public String postContact(ContactForm contactForm, Model model) {
		Mail mail = new Mail(Mail.TO_SUPPORT, Mail.SUB_CONTACT);
		mail.setContent(contactForm.getContent());
		emailSender.receiveMail(mail);
		Mail reply = new Mail(contactForm.getFrom(), Mail.SUB_REPLY_TO_CONTACT);
		emailSender.sendMail(reply);
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		model.addAttribute(contactForm);
		return "about/contact/sentContact";
	}
	
	@RequestMapping("/line-notify/apply")
	public String applyLineNotify(@RequestParam(name="from") String from) {
		if (from.equals("mypage")) {
			String uri = lineNotify.generateRedirectUriToGetCode(this.CONTEXT_PATH + "/line-notify/oauth/to-mypage");
			return "redirect:" + uri;
		} else {
			String uri = lineNotify.generateRedirectUriToGetCode(this.CONTEXT_PATH + "/line-notify/oauth/to-index");
			return "redirect:" + uri;
		}
	}
	
	@RequestMapping("/line-notify/oauth/to-index")
	public String postCode(@RequestParam(name="code", required=false) String code, @RequestParam(name="state", required=false) String state, @RequestParam(name="error", required=false) String error, @RequestParam(name="error_description", required=false) String error_description, Model model) {
		if (Objects.equals(code, null) || !(passwordEncoder.matches(securityService.findLoggedInEmail(), state))) {
			throw new NotFound404();
		} else {
			String loggedInEmail = securityService.findLoggedInEmail();
			String accessToken = lineNotify.getAccessToken(code, this.CONTEXT_PATH + "/line-notify/oauth/to-index");
			accountService.changeLineAccessToken(loggedInEmail, accessToken);
			lineNotify.notifyMessage(accessToken, lineNotify.getMessage(new Mail(loggedInEmail, Mail.SUB_WELCOME_TO_US)));
			return "redirect:/";
		}
	}
	
	@RequestMapping("/line-notify/oauth/to-mypage")
	public String postCodeFromMypage(@RequestParam(name="code", required=false) String code, @RequestParam(name="state", required=false) String state, @RequestParam(name="error", required=false) String error, @RequestParam(name="error_description", required=false) String error_description, Model model) {
		if (Objects.equals(code, null) || !(passwordEncoder.matches(securityService.findLoggedInEmail(), state))) {
			throw new NotFound404();
		} else {
			String loggedInEmail = securityService.findLoggedInEmail();
			String accessToken = lineNotify.getAccessToken(code, this.CONTEXT_PATH + "/line-notify/oauth/to-mypage");
			accountService.changeLineAccessToken(loggedInEmail, accessToken);
			lineNotify.notifyMessage(accessToken, lineNotify.getMessage(new Mail(loggedInEmail, Mail.SUB_WELCOME_TO_US)));
			return "redirect:/user/account";
		}
	}
	
	@RequestMapping("/line-notify/revoke")
	public String revokeLineNotify() {
		String loggedInEmail = securityService.findLoggedInEmail();
		String accessToken = accountService.getLineAccessTokenByEmail(loggedInEmail);
		lineNotify.revoke(accessToken);
		accountService.changeLineAccessToken(loggedInEmail, null);
		return "redirect:/user/account";
	}
	
	@RequestMapping("/help/get-email")
	public String getHowToGetEmail() {
		return "help/howToGetEmail";
	}
}