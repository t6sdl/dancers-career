package tokyo.t6sdl.dancerscareer2019.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import tokyo.t6sdl.dancerscareer2019.model.Account;

@Service
public class SecurityService {
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	
	public SecurityService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
	}
	
	public void autoLogin(String email, String rawPassword) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, rawPassword, userDetails.getAuthorities());
		authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
	}
	
	public String findLoggedInEmail() {
		Object account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (account instanceof Account) {
			return ((Account) account).getEmail();
		} else {
			return "";
		}
	}
	
	public String findLoggedInPassword() {
		Object account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (account instanceof Account) {
			return ((Account) account).getPassword();
		} else {
			return "";
		}
	}
	
	public boolean findLoggedInValidEmail() {
		Object account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (account instanceof Account) {
			return ((Account) account).isValid_email();
		} else {
			return false;
		}
	}
	
	public boolean findLoggedInAuthority() {
		Object account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (account instanceof Account) {
			return ((Account) account).getAuthority().equals("ROLE_ADMIN");
		} else {
			return false;
		}
	}
	
	public String findLoggedInEmailToken() {
		Object account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (account instanceof Account) {
			return ((Account) account).getEmail_token();
		} else {
			return "";
		}
	}
	
	public String findLoggedInPasswordToken() {
		Object account = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (account instanceof Account) {
			return ((Account) account).getPassword_token();
		} else {
			return "";
		}
	}
}