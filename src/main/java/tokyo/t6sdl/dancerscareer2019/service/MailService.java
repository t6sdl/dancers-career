package tokyo.t6sdl.dancerscareer2019.service;

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
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;

@RequiredArgsConstructor
@Service
public class MailService {
	private final JavaMailSender mailSender;
	private final AccountRepository accountRepository;
	private final LineNotifyService lineNotify;
	
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
			String accessToken = accountRepository.findLineAccessTokenByEmail(mail.getTo());
			if (!(Objects.equals(accessToken, null))) {
				lineNotify.notifyMessage(accessToken, lineNotify.getMessage(mail));
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
			return "/mails/welcome-to-us?url=" + mail.getUrl();
		case Mail.SUB_VERIFY_EMAIL:
			return "/mails/verify-email?url=" + mail.getUrl();
		case Mail.SUB_RESET_PWD:
			return "/mails/forget-pwd?url=" + mail.getUrl();
		case Mail.SUB_REPLY_TO_CONTACT:
			return "/mails/reply-to-contact";
		default:
			throw new IllegalArgumentException();
		}
	}
}
