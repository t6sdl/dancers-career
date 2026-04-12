package tokyo.t6sdl.dancerscareer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import tokyo.t6sdl.dancerscareer.model.Mail;

@Component("mailSettings")
public class MailSettings {
	@Value("${MAIL_FROM_ADDRESS:}")
	private String configuredFromAddress;

	@Value("${MAILGUN_DOMAIN:}")
	private String mailgunDomain;

	@Value("${MAILGUN_SENDING_KEY:}")
	private String mailgunSendingKey;

	@Value("${MAILGUN_API_BASE_URL:https://api.mailgun.net}")
	private String mailgunApiBaseUrl;

	public String getFromAddress() {
		if (StringUtils.hasText(configuredFromAddress)) {
			return configuredFromAddress;
		}
		if (StringUtils.hasText(mailgunDomain)) {
			return "postmaster@" + mailgunDomain;
		}
		throw new IllegalStateException("MAIL_FROM_ADDRESS or MAILGUN_DOMAIN must be set");
	}

	public String getFormattedFrom() {
		return Mail.NAME_OF_SUPPORT + " <" + getFromAddress() + ">";
	}

	public boolean isMailgunApiEnabled() {
		return StringUtils.hasText(mailgunDomain) && StringUtils.hasText(mailgunSendingKey);
	}

	public String getMailgunDomain() {
		return mailgunDomain;
	}

	public String getMailgunSendingKey() {
		return mailgunSendingKey;
	}

	public String getMailgunApiBaseUrl() {
		return mailgunApiBaseUrl;
	}
}
