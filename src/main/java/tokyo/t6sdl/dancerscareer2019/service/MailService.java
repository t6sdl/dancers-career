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
	
	public void sendMailWithoutUrl(String to, String subject) {
		try {
			String content = this.createContent(subject, "");
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
				default:
					break;
			}
		}
		draft.append("</body></html>");
		content = draft.toString();
		return content;
	}
}
