package tokyo.t6sdl.dancerscareer2019.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;

@Async
@RequiredArgsConstructor
@Component
public class EmailSender {
	private final JavaMailSender mailSender;
	private final AccountService accountService;
	private final LineNotifyManager lineNotify;
		
	public void sendMail(Mail mail) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			message.setHeader("Content-type", "text/html");
			message.setHeader("Errors-To", Mail.TO_ERROR);
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(Mail.TO_SUPPORT, Mail.NAME_OF_SUPPORT);
			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			this.readContent(mail);
			helper.setText(mail.getContent(), true);
			mailSender.send(message);
			String accessToken = accountService.getLineAccessTokenByEmail(mail.getTo());
			if (!(Objects.equals(accessToken, null)) || !(accessToken.isEmpty())) {
				lineNotify.notifyMessage(accessToken, lineNotify.getMessage(mail));
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMailWithToken(Mail mail) throws Exception {
		try {
			String token;
			switch (mail.getSubject()) {
			case Mail.SUB_WELCOME_TO_US:
			case Mail.SUB_VERIFY_EMAIL:
				token = accountService.createEmailToken(mail.getTo());
				break;
			case Mail.SUB_RESET_PWD:
				token = accountService.createPasswordToken(mail.getTo());
				break;
			default:
				throw new Exception();
			}
			if (token.isEmpty()) {
				throw new Exception();
			}
			MimeMessage message = mailSender.createMimeMessage();
			message.setHeader("Content-type", "text/html");
			message.setHeader("Errors-To", Mail.TO_ERROR);
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(Mail.TO_SUPPORT, Mail.NAME_OF_SUPPORT);
			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			this.readContent(mail);
			helper.setText(mail.getContent(), true);
			mailSender.send(message);
			String accessToken = accountService.getLineAccessTokenByEmail(mail.getTo());
			if (!(Objects.equals(accessToken, null)) || !(accessToken.isEmpty())) {
				lineNotify.notifyMessage(accessToken, lineNotify.getMessage(mail));
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMailMagazine(Mail mail) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			message.setHeader("Content-type", "text/html");
			message.setHeader("Errors-To", Mail.TO_ERROR);
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setFrom(Mail.TO_SUPPORT, Mail.NAME_OF_SUPPORT);
			helper.setSubject(mail.getSubject());
			this.readContent(mail);
			helper.setText(mail.getContent(), true);
			String lineText = lineNotify.getMessage(mail);
			for (Account account : mail.getAccounts()) {
				helper.setTo(account.getEmail());
				mailSender.send(message);
				if (!(Objects.equals(account.getLine_access_token(), null)) && !(account.getLine_access_token().isEmpty())) {
					lineNotify.notifyMessage(account.getLine_access_token(), lineText);
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveMail(Mail mail) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			message.setHeader("Content-type", "text/html");
			message.setHeader("Errors-To", Mail.TO_ERROR);
			MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
			helper.setFrom(Mail.TO_SUPPORT, Mail.NAME_OF_SUPPORT);
			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getContent(), false);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	protected void readContent(Mail mail) {
		StringBuffer draft = new StringBuffer();
		URL url = null;
		InputStreamReader isr = null;
		try {
			url = new URL(Mail.CONTEXT_PATH + this.getHtmlSource(mail));
			InputStream is = url.openStream();
			isr = new InputStreamReader(is, "UTF-8");
			while (true) {
				int i = isr.read();
				if (i == -1) {
					break;
				}
				draft.append((char)i);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mail.setContent(draft.toString());
	}
	
	private String getHtmlSource(Mail mail) {
		switch (mail.getSubject()) {
		case Mail.SUB_WELCOME_TO_US:
			return "/mails/welcome-to-us?to=" + mail.getTo();
		case Mail.SUB_VERIFY_EMAIL:
			return "/mails/verify-email?to=" + mail.getTo();
		case Mail.SUB_RESET_PWD:
			return "/mails/forget-pwd?to=" + mail.getTo();
		case Mail.SUB_REPLY_TO_CONTACT:
			return "/mails/reply-to-contact";
		case Mail.SUB_NEW_ES:
			return "/mails/new-es-mail";
		default:
			throw new IllegalArgumentException();
		}
	}
}
