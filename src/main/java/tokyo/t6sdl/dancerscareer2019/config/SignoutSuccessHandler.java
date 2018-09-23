package tokyo.t6sdl.dancerscareer2019.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;

@Component
public class SignoutSuccessHandler
		implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
	private AccountService accountService;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		Object account = authentication.getPrincipal();
		if (account instanceof Account) {
			accountService.changeLastLogin(((Account) account).getEmail());
		}
		response.sendRedirect("/signin");
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
}
