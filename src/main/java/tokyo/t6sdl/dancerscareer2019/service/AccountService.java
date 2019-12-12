package tokyo.t6sdl.dancerscareer2019.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {
	private final AccountRepository accountRepository;
	private PasswordEncoder passwordEncoder;
		
	public Account getAccountByEmail(String email) {
		if (Objects.equals(email, null)) {
			return null;
		}
		return accountRepository.findOneByEmail(email);
	}
	
	public String getEmailTokenByEmail(String email) {
		return accountRepository.findEmailTokenByEmail(email);
	}
	
	public String getPasswordTokenByEmail(String email) {
		return accountRepository.findPasswordTokenByEmail(email);
	}
	
	public String getLineAccessTokenByEmail(String email) {
		return accountRepository.findLineAccessTokenByEmail(email);
	}
	
	public void create(Account newAccount, String rawPassword) {
		String encodedPassword = passwordEncoder.encode(rawPassword);
		newAccount.setPassword(encodedPassword);
		accountRepository.insert(newAccount);
	}
	
	public void delete(String email) {
		accountRepository.delete(email);
	}
	
	public void changeEmail(String loggedInEmail, String newEmail) {
		accountRepository.updateEmail(loggedInEmail, newEmail);
	}
	
	public void changePassword(String loggedInEmail, String newRawPassword) {
		String newEncodedPassword = passwordEncoder.encode(newRawPassword);
		accountRepository.updatePassword(loggedInEmail, newEncodedPassword);
	}
	
	public void changeValidEmail(String loggedInEmail, boolean validEmail) {
		accountRepository.updateValidEmail(loggedInEmail, validEmail);
	}
	
	public void changeLastLogin(String loggedInEmail) {
		accountRepository.updateLastLogin(loggedInEmail);
	}
	
	public void changeLineAccessToken(String loggedInEmail, String lineAccessToken) {
		accountRepository.updateLineAccessToken(loggedInEmail, lineAccessToken);
	}
	
	public void changeNewEsMail(String loggedInEmail, boolean newEsMail) {
		accountRepository.updateNewEsMail(loggedInEmail, newEsMail);
	}
	
	public String createEmailToken(String loggedInEmail) {
		String emailToken = passwordEncoder.encode(loggedInEmail);
		accountRepository.recordEmailToken(loggedInEmail, emailToken);
		return emailToken;
	}
	
	public String createPasswordToken(String loggedInEmail) {
		String passwordToken = passwordEncoder.encode(loggedInEmail);
		accountRepository.recordPasswordToken(loggedInEmail, passwordToken);
		return passwordToken;
	}
	
	public boolean isValidEmailToken(String emailToken) {
		Account account = accountRepository.findOneByEmailToken(emailToken);
		if (!(account instanceof Account)) {
			return false;
		}
		if (!(account.isValidEmail())) {
			accountRepository.updateValidEmail(account.getEmail(), true);
		}
		return true;
	}
	
	public boolean isValidPasswordToken(String passwordToken) {
		Account account = accountRepository.findOneByPasswordToken(passwordToken);
		if (!(account instanceof Account)) {
			return false;
		}
		LocalDateTime expiration = account.getUpdatedAt().plusMinutes(30);
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
		if (now.isAfter(expiration)) {
			return false;
		} else {
			return true;
		}
	}
	
	public String completeResetPassword(String passwordToken) {
		Account account = accountRepository.findOneByPasswordToken(passwordToken);
		if (!(account instanceof Account)) {
			return "";
		}
		accountRepository.refreshPasswordToken(account.getEmail());
		return account.getEmail();
	}
	
	public void refreshToken(String loggedInEmail) {
		accountRepository.refreshEmailToken(loggedInEmail);
		accountRepository.refreshPasswordToken(loggedInEmail);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = null;
		try {
			account = accountRepository.findOneByEmail(email);
		} catch (Exception e) {
			throw new UsernameNotFoundException("もう一度お試しください");
		}
		if (account == null) {
			throw new UsernameNotFoundException("このユーザーは存在しません");
		}
		return account;
	}
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}