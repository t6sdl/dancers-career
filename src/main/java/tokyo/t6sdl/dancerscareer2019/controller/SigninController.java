package tokyo.t6sdl.dancerscareer2019.controller;

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
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.io.EmailSender;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.form.EmailForm;
import tokyo.t6sdl.dancerscareer2019.model.form.NewPasswordForm;
import tokyo.t6sdl.dancerscareer2019.model.form.SigninForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/signin")
public class SigninController {
	private final SecurityService securityService;
	private final AccountService accountService;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@GetMapping()
	public String getSignin(@RequestParam(name="from", required=false) String from, Model model) {
		if (Objects.equals(from, null)) {
			return "signin/signinForm";
		} else if (from.equals("mail-setting")) {
			SigninForm form = new SigninForm();
			form.setFrom(from);
			model.addAttribute(form);
			return "common/verify";
		}
		return "signin/signinForm";
	}
	
	@PostMapping()
	public String postSignin(@Validated SigninForm form, BindingResult result, Model model) {
		if (Objects.equals(form.getFrom(), null) || form.getFrom().isEmpty()) throw new NotFound404();
		if (result.hasErrors()) return "redirect:/signin?from=" + form.getFrom() + "&error";
		Account account = accountService.getAccountByEmail(form.getEmail());
		if (Objects.equals(account, null) || !passwordEncoder.matches(form.getPassword(), account.getPassword())) {
			return "redirect:/signin?from=" + form.getFrom() + "&error";
		}
		securityService.autoLogin(form.getEmail(), form.getPassword());
		if (account.isAdmin()) {
			return "redirect:/admin";
		} else if (form.getFrom().equals("mail-setting")) {
			return "redirect:/user/account/change/mail-setting";
		}
		return "redirect:/";
	}

	@GetMapping("/forget-pwd")
	public String getFogetPassword(@RequestParam(name = "token", required = false) String token, Model model) {
		if (Objects.equals(token, null) || token.isEmpty()) {
			model.addAttribute(new EmailForm());
			return "signin/forgetPassword";
		} else if (accountService.isValidPasswordToken(token)) {
			model.addAttribute(new NewPasswordForm());
			model.addAttribute("token", token);
			return "signin/resetPassword";
		} else {
			throw new NotFound404();
		}
	}

	@PostMapping("/forget-pwd")
	public String postForgetPassword(@Validated EmailForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "signin/forgetPassword";
		}
		Mail mail = new Mail(form.getEmail(), Mail.SUB_RESET_PWD);
		try {
			emailSender.sendMailWithToken(mail);
		} catch (Exception e) {
			model.addAttribute("error", true);
			return "signin/sentEmail";
		}
		model.addAttribute("error", false);
		return "signin/sentEmail";
	}

	@PostMapping("/reset-pwd")
	public String postResetPassword(@Validated NewPasswordForm form, BindingResult result, @RequestParam("token") String token, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("token", token);
			return "signin/resetPassword";
		}
		String email = accountService.completeResetPassword(token);
		accountService.changePassword(email, form.getNewPassword());
		return "signin/completeResetPassword";
	}
}