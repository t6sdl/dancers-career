package tokyo.t6sdl.dancerscareer2019.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Mail;

@RequiredArgsConstructor
@Service
public class MailService {
	private final JavaMailSender mailSender;
		
	public void sendMailWithUrl(String to, String subject, String url) {
		try {
			String content = this.createContent(subject, url);
			MimeMessage mail = mailSender.createMimeMessage();
			mail.setHeader("Content-type", "text/html");
			mail.setHeader("Errors-To", Mail.TO_ERROR);
			MimeMessageHelper helper = new MimeMessageHelper(mail, false, "UTF-8");
			helper.setFrom(Mail.TO_SUPPORT, Mail.NAME_OF_SUPPORT);
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
	
	public void sendMailWithoutUrl(String to, String subject) {
		try {
			String content = this.createContent(subject, "");
			MimeMessage mail = mailSender.createMimeMessage();
			mail.setHeader("Content-type", "text/html");
			mail.setHeader("Errors-To", Mail.TO_ERROR);
			MimeMessageHelper helper = new MimeMessageHelper(mail, false, "UTF-8");
			helper.setFrom(Mail.TO_SUPPORT, Mail.NAME_OF_SUPPORT);
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
	
	public String createContent(String subject, String url) {
		String content;
		StringBuffer draft = new StringBuffer();
		draft.append("<!DOCTYPE html><html><body>");
		if (url != "") {
			switch (subject) {
			case Mail.SUB_VERIFY_EMAIL:
				draft.append("<a href='" + url + "'><button>メールアドレスの確認</button></a>");
				break;
			case Mail.SUB_RESET_PWD:
				draft.append("<a href='" + url + "'><button>パスワードの再設定</button></a>");
			default:
				break;
			}
		} else {
			switch (subject) {
			case Mail.SUB_REPLY_TO_CONTACT:
				draft.append("<h1>お問い合わせありがとうございます<h1><p>後ほどダンサーズキャリア事務局からご連絡いたします。ご連絡には数日程度かかる場合がございます。ご了承ください。</p>");
			default:
				break;
			}
		}
		draft.append("</body></html>");
		content = draft.toString();
		return content;
	}
	
	public void receiveMail(String to, String subject, String content) {
		try {
			MimeMessage mail = mailSender.createMimeMessage();
			mail.setHeader("Content-type", "text/html");
			mail.setHeader("Errors-To", Mail.TO_ERROR);
			MimeMessageHelper helper = new MimeMessageHelper(mail, false, "UTF-8");
			helper.setFrom(Mail.TO_SUPPORT, Mail.NAME_OF_SUPPORT);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, false);
			mailSender.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
