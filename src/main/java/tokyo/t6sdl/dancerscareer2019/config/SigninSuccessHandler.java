package tokyo.t6sdl.dancerscareer2019.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;

@Component
public class SigninSuccessHandler implements AuthenticationSuccessHandler {
	private AccountService accountService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Object account = authentication.getPrincipal();
		if (account instanceof Account) {
			accountService.changeLastLogin(((Account) account).getEmail());
			LocalDateTime updatedAt = ((Account) account).getUpdated_at();
			LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
			if (now.isAfter(updatedAt.plusMinutes(30)) && (!(Objects.equals(((Account) account).getEmail_token(), null)) || !(Objects.equals(((Account) account).getPassword_token(), null)))) {
				accountService.refreshToken(((Account) account).getEmail());
			}
			if (((Account) account).isAdmin()) {
				response.sendRedirect("/admin");
			} else {
				response.sendRedirect("/");
			}
		} else {
			response.sendError(403);
		}
	}
	
	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
}
