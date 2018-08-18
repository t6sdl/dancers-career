package tokyo.t6sdl.dancerscareer2019.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tokyo.t6sdl.dancerscareer2019.model.EmailForm;
import tokyo.t6sdl.dancerscareer2019.model.PasswordForm;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;

@Controller
@RequestMapping("/signin")
public class SigninController {
	private final AccountService accountService;
	
	public SigninController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping
	public String getSignin() {
		return "signin/signinForm";
	}
	
	@PostMapping
	public String postSignin() {
		return "redirect:/";
	}
	
	@GetMapping("/forget-pwd")
	public String getFogetPassword(@RequestParam(name = "token", required = false) String token, Model model) {
		if (token == "" || token == null) {
			model.addAttribute(new EmailForm());
			return "signin/forgetPassword";
		}
		model.addAttribute(new PasswordForm());
		model.addAttribute("token", token);
		return "signin/resetPassword";
	}
	
	@PostMapping("/forget-pwd")
	public String postForgetPassword(@Validated EmailForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "signin/forgetPassword";
		}
		return "signin/sentEmail";
	}
		
	@PostMapping("/reset-pwd")
	public String postResetPassword(@Validated PasswordForm form, BindingResult result, @RequestParam("token") String token, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("token", token);
			return "signin/resetPassword";
		}
		return "redirect:/signin/reset-pwd-completed";
	}
	
	@RequestMapping("/reset-pwd-completed")
	public String completeResetPassword() {
		return "signin/completeResetPassword";
	}
}