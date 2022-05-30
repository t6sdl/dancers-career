package tokyo.t6sdl.dancerscareer.config;

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

import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.service.AccountService;

@Component
public class SigninSuccessHandler implements AuthenticationSuccessHandler {
	private AccountService accountService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Object account = authentication.getPrincipal();
		String from = request.getParameter("from");
		if (!(account instanceof Account)) {
			response.sendError(403);
		}
		accountService.changeLastLogin(((Account) account).getEmail());
		LocalDateTime updatedAt = ((Account) account).getUpdatedAt();
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		if (now.isAfter(updatedAt.plusMinutes(30)) && (!(Objects.equals(((Account) account).getEmailToken(), null)) || !(Objects.equals(((Account) account).getPasswordToken(), null)))) {
			accountService.refreshToken(((Account) account).getEmail());
		}
		if (((Account) account).isAdmin()) {
			response.sendRedirect("/admin");
		} else if (Objects.equals(from, "mail-setting")) {
			response.sendRedirect("/user/account/change/mail-setting");
		} else {
			response.sendRedirect("/");
		}
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
}
