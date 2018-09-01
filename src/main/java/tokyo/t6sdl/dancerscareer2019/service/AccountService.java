package tokyo.t6sdl.dancerscareer2019.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;

@Service
public class AccountService implements UserDetailsService {
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	
	public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
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
	
	public void changePassword(String loggedInEmail, String newPassword) {
		String newEncodedPassword = passwordEncoder.encode(newPassword);
		accountRepository.updatePassword(loggedInEmail, newEncodedPassword);
	}
	
	public void changeValidEmail(String loggedInEmail, boolean validEmail) {
		accountRepository.updateValidEmail(loggedInEmail, validEmail);
	}
	
	public String createEmailToken(String loggedInEmail) {
		String emailToken;
		emailToken = createAccountToken();
		if (emailToken == "") {
			return "";
		} else {
			accountRepository.recordEmailToken(loggedInEmail, emailToken);
			return emailToken;
		}
	}
	
	public String createPasswordToken(String loggedInEmail) {
		String passwordToken;
		passwordToken = createAccountToken();
		if (passwordToken == "") {
			return "";
		} else {
			accountRepository.recordPasswordToken(loggedInEmail, passwordToken);
			return passwordToken;
		}
	}
	
	public boolean isValidEmailToken(String emailToken) {
		Account account = accountRepository.findOneByEmailToken(emailToken);
		if (account == null) {
			return false;
		}
		accountRepository.refreshEmailToken(account.getEmail());
		accountRepository.updateValidEmail(account.getEmail(), true);
		return true;
	}
	
	public boolean isValidPasswordToken(String passwordToken) {
		Account account = accountRepository.findOneByPasswordToken(passwordToken);
		if (account == null) {
			return false;
		}
		LocalDateTime expiration = account.getUpdated_at().plusMinutes(30);
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(expiration)) {
			return false;
		} else {
			return true;
		}
	}
	
	public String completeResetPassword(String passwordToken) {
		Account account = accountRepository.findOneByPasswordToken(passwordToken);
		if (account == null) {
			return "";
		}
		accountRepository.refreshPasswordToken(account.getEmail());
		return account.getEmail();
	}
	
	private String createAccountToken() {
		try {
			SecureRandom random = SecureRandom.getInstanceStrong();
			byte[] bytes = new byte[20];
			random.nextBytes(bytes);
			StringBuffer s = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				s.append(String.format("%02x", bytes[i]));
			}
			String token = s.toString();
			return token;
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
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
}