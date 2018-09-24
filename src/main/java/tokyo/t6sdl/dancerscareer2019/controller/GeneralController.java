package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.model.AccessToken;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.ParamForLineOAuth;
import tokyo.t6sdl.dancerscareer2019.model.form.ContactForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.MailService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
public class GeneralController {
	private final SecurityService securityService;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;
	
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
	
	@RequestMapping("/line-notify/apply")
	public String applyLineNotify() {
		String state = passwordEncoder.encode(securityService.findLoggedInEmail());
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl("https://notify-bot.line.me/oauth/authorize");
		uri.queryParam("response_type", "code");
		uri.queryParam("client_id", "hjr1WCDvkmDhaomQuOMwmD");
		uri.queryParam("redirect_uri", "https://dancers-career-2019-stg.herokuapp.com/line-notify/oauth/authorize");
		uri.queryParam("scope", "notify");
		uri.queryParam("state", state);
		uri.queryParam("response_mode", "form_post");
		return "redirect:" + uri.toUriString();
	}
	
	@PostMapping("/line-notify/oauth")
	public String postCode(@RequestParam(name="code", required=false) String code, @RequestParam(name="state", required=false) String state, @RequestParam(name="error", required=false) String error, @RequestParam(name="error_description", required=false) String error_description, Model model) {
		if (Objects.equals(code, null) || passwordEncoder.matches(securityService.findLoggedInEmail(), state)) {
			throw new NotFound404();
		} else {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			ParamForLineOAuth params = new ParamForLineOAuth("authorization_code", code, "https://dancers-career-2019-stg.herokuapp.com/line-notify/oauth/token", "hjr1WCDvkmDhaomQuOMwmD", "BmCuA1Ca9NPxbBCFhhp24QFr6cKc54sflO0Pl481eYy");
			HttpEntity<ParamForLineOAuth> entity = new HttpEntity<ParamForLineOAuth>(params, headers);
			AccessToken token = restTemplate.postForObject("https://notify-bot.line.me/oauth/token", entity, AccessToken.class);
			accountService.changeLineAccessToken(securityService.findLoggedInEmail(), token.getAccess_token());
			model.addAttribute("access_token", token.getAccess_token());
			return "index/index";
		}
	}
}