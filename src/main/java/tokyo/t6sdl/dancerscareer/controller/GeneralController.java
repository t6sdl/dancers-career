package tokyo.t6sdl.dancerscareer.controller;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
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
import tokyo.t6sdl.dancerscareer.io.LineNotifyManager;
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
		Mail ask = new Mail(Mail.TO_SUPPORT, Mail.SUB_CONTACT);
		ask.setContent("[Email: " + contactForm.getFrom() + "]\n" + contactForm.getContent());
		emailSender.receiveMail(ask);
		Mail reply = new Mail(contactForm.getFrom(), Mail.SUB_REPLY_TO_CONTACT);
		emailSender.sendMail(reply);
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
