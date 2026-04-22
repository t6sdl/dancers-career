package tokyo.t6sdl.dancerscareer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.config.MailSettings;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.model.Mail;
import tokyo.t6sdl.dancerscareer.service.AccountService;

@Async
@RequiredArgsConstructor
@Component
public class EmailSender {
	private final AccountService accountService;
	private final MailSettings mailSettings;
	private final RestTemplate restTemplate = new RestTemplate();

	public void sendContactForm(Mail reply, Mail ask) {
		boolean isSent = sendMail(reply);
		if (isSent) {
			receiveMail(ask);
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
			this.readContent(mail);
			sendHtmlMessage(mail.getTo(), mail.getSubject(), mail.getContent());
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void sendMassMail(Mail mail) {
		try {
			this.readContent(mail);
			for (Account account : mail.getAccounts()) {
				sendHtmlMessage(account.getEmail(), mail.getSubject(), mail.getContent());
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void sendMassTextMail(Mail mail) {
		try {
			for (Account account : mail.getAccounts()) {
				sendTextMessage(account.getEmail(), mail.getSubject(), mail.getContent());
			}
		} catch (RuntimeException e) {
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
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mail.setContent(draft.toString());
	}

	private boolean sendMail(Mail mail) {
		try {
			this.readContent(mail);
			sendHtmlMessage(mail.getTo(), mail.getSubject(), mail.getContent());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void receiveMail(Mail mail) {
		try {
			sendTextMessage(mail.getTo(), mail.getSubject(), mail.getContent());
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
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
		case Mail.SUB_SURVEY:
			return "/mails/survey";
		default:
			throw new IllegalArgumentException();
		}
	}

	private void sendHtmlMessage(String to, String subject, String html) {
		sendViaMailgunApi(to, subject, html, true);
	}

	private void sendTextMessage(String to, String subject, String text) {
		sendViaMailgunApi(to, subject, text, false);
	}

	private void sendViaMailgunApi(String to, String subject, String content, boolean html) {
		if (!mailSettings.isMailgunApiEnabled()) {
			throw new IllegalStateException("MAILGUN_SENDING_KEY and MAILGUN_DOMAIN must be set");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("api", mailSettings.getMailgunSendingKey());
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("from", mailSettings.getFormattedFrom());
		body.add("to", to);
		body.add("subject", subject);
		body.add("h:Errors-To", Mail.TO_ERROR);
		body.add("h:Reply-To", Mail.TO_SUPPORT);
		if (html) {
			body.add("html", content);
		} else {
			body.add("text", content);
		}
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
		String endpoint = mailSettings.getMailgunApiBaseUrl() + "/v3/" + mailSettings.getMailgunDomain() + "/messages";
		ResponseEntity<String> response;
		try {
			response = restTemplate.postForEntity(endpoint, request, String.class);
		} catch (RestClientException e) {
			throw new IllegalStateException("Failed to send mail via Mailgun API", e);
		}
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new IllegalStateException("Mailgun API returned status " + response.getStatusCodeValue());
		}
	}
}
