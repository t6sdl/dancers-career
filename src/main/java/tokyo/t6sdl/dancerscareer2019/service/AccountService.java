package tokyo.t6sdl.dancerscareer2019.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
	
	public void changeEmail(String loggedInUsername, String newEmail) {
		accountRepository.updateEmail(loggedInUsername, newEmail);
	}
	
	public void changePassword(String loggedInUsername, String newPassword) {
		accountRepository.updatePassword(loggedInUsername, newPassword);
	}
	
	public void changeEnabled(String loggedInUsername, boolean isEnabled) {
		accountRepository.updateEnabled(loggedInUsername, isEnabled);
	}
	
	public String createEmailToken(String loggedInUsername) throws NoSuchAlgorithmException {
		String emailToken = createAccountToken();
		if (emailToken == "") {
			return "";
		} else {
			accountRepository.recordEmailToken(loggedInUsername, emailToken);
			return emailToken;
		}
	}
	
	public String createPasswordToken(String loggedInUsername) throws NoSuchAlgorithmException {
		String passwordToken = createAccountToken();
		if (passwordToken == "") {
			return "";
		} else {
			accountRepository.recordPasswordToken(loggedInUsername, passwordToken);
			return passwordToken;
		}
	}
	
	public boolean isValidEmailToken(String emailToken) {
		boolean result;
		Account account = accountRepository.findOneByEmailToken(emailToken);
		if (account != null) {
			result = true;
			accountRepository.refreshEmailToken(account.getEmail());
		} else {
			result = false;
		}
		return result;
	}
	
	public boolean isValidPasswordToken(String passwordToken) {
		boolean result;
		Account account = accountRepository.findOneByPasswordToken(passwordToken);
		if (account != null) {
			result = true;
			accountRepository.refreshPasswordToken(account.getEmail());
		} else {
			result = false;
		}
		return result;
	}
	
	public String createAccountToken() throws NoSuchAlgorithmException {
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