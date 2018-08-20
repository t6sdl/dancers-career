package tokyo.t6sdl.dancerscareer2019.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
	private final JavaMailSender mailSender;
	
	public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
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
		accountRepository.updatePassword(loggedInEmail, newPassword);
	}
	
	public void changeValidEmail(String loggedInEmail, boolean validEmail) {
		accountRepository.updateValidEmail(loggedInEmail, validEmail);
	}
	
	public String createEmailToken(String loggedInEmail) {
		String emailToken;
		try {
			emailToken = createAccountToken();
			if (emailToken == "") {
				return "";
			} else {
				accountRepository.recordEmailToken(loggedInEmail, emailToken);
				return emailToken;
			}
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public String createPasswordToken(String loggedInEmail) {
		String passwordToken;
		try {
			passwordToken = createAccountToken();
			if (passwordToken == "") {
				return "";
			} else {
				accountRepository.recordPasswordToken(loggedInEmail, passwordToken);
				return passwordToken;
			}
		} catch (NoSuchAlgorithmException e) {
			return null;
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
	
	public void sendMail(String to, String subject, String content) {
		try {
			MimeMessage mail = mailSender.createMimeMessage();
			mail.setHeader("Content-type", "text/html");
			MimeMessageHelper helper = new MimeMessageHelper(mail, false, "UTF-8");
			helper.setFrom("test_dancerscareer@t6sdl.tokyo", "（テスト）ダンサーズキャリア事務局");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}