package tokyo.t6sdl.dancerscareer.io;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.model.AccessToken;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.model.Experience;
import tokyo.t6sdl.dancerscareer.model.Mail;
import tokyo.t6sdl.dancerscareer.model.Notify;
import tokyo.t6sdl.dancerscareer.service.AccountService;
import tokyo.t6sdl.dancerscareer.service.SecurityService;

@RequiredArgsConstructor
@Component
public class LineNotifyManager {
	private final SecurityService securityService;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;

	public String generateRedirectUriToGetCode(String callback) {
		UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl("https://notify-bot.line.me/oauth/authorize");
		uri.queryParam("response_type", "code");
		uri.queryParam("client_id", "hjr1WCDvkmDhaomQuOMwmD");
		uri.queryParam("redirect_uri", callback);
		uri.queryParam("scope", "notify");
		uri.queryParam("state", passwordEncoder.encode(securityService.findLoggedInEmail()));
		return uri.build().toUriString();
	}

	public String getAccessToken(String code, String callback) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("grant_type", "authorization_code");
		map.add("code", code);
		map.add("redirect_uri", callback);
		map.add("client_id", "hjr1WCDvkmDhaomQuOMwmD");
		map.add("client_secret", "QrBCVmNvn79CfHmHfnK8yG44oxloL0llEQpSP7ZmrDo");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		AccessToken token = restTemplate.postForObject("https://notify-bot.line.me/oauth/token", entity, AccessToken.class);
		return token.getAccess_token();
	}

	public int notifyMessage(String accessToken, String message) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", "Bearer " + accessToken);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("message", message);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			Notify notify = restTemplate.postForObject("https://notify-api.line.me/api/notify", entity, Notify.class);
			return notify.getStatus();
		} catch (HttpClientErrorException e) {
			return e.getRawStatusCode();
		}
	}

	public int getTokenStatus(String accessToken) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + accessToken);
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			ResponseEntity<Notify> response = restTemplate.exchange("https://notify-api.line.me/api/status", HttpMethod.GET, entity, Notify.class);
			Notify notify = response.getBody();
			return notify.getStatus();
		} catch (HttpClientErrorException e) {
			return e.getRawStatusCode();
		}
	}

	public int revoke(String accessToken) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set("Authorization", "Bearer " + accessToken);
			HttpEntity<?> entity = new HttpEntity<Object>(headers);
			Notify notify = restTemplate.postForObject("https://notify-api.line.me/api/revoke", entity, Notify.class);
			return notify.getStatus();
		} catch (HttpClientErrorException e) {
			return e.getRawStatusCode();
		}
	}

	public String getMessage(Mail mail) {
		Account account = accountService.getAccountByEmail(mail.getTo());
		StringBuilder draft = new StringBuilder();
		draft.append("\n\n");
		switch (mail.getSubject()) {
		case Mail.SUB_WELCOME_TO_US:
			draft.append("ダンサーズキャリアにご登録いただきありがとうございます！\n");
			draft.append("今後はダンサーズキャリアからのメールが届くと、LINEへもメッセージが届きます！\n\n");
			if (!(account.isValidEmail())) {
				draft.append("↓下記のURLからメールアドレスの確認をお済ませください。\n");
				draft.append(Mail.URI_VERIFY_EMAIL + account.getEmailToken() + "\n\n");
			}
			break;
		case Mail.SUB_VERIFY_EMAIL:
			draft.append("↓下記のURLからメールアドレスの確認をお済ませください。\n\n");
			draft.append(Mail.URI_VERIFY_EMAIL + account.getEmailToken() + "\n\n");
			break;
		case Mail.SUB_RESET_PWD:
			draft.append("↓下記のURLからパスワードの再設定ができます。\nURLの有効期限は30分です。\n\n");
			draft.append(Mail.URI_RESET_PWD + account.getPasswordToken() + "\n\n");
			break;
		case Mail.SUB_REPLY_TO_CONTACT:
			draft.append("お問い合わせいただきありがとうございます。\nお返事に数日程度かかる場合もございます。ご了承ください。\n\n");
			break;
		case Mail.SUB_NEW_ES:
			List<Experience> experiences = mail.getExperiences();
			draft.append(experiences.size() + "件の新着のES/体験記があります！\n\n");
			for (Experience experience : experiences) {
				draft.append("★☆ ");
				for (String pos : experience.getPosition()) {
					draft.append("[[" + pos + "]] ");
				}
				draft.append(experience.getUnivName() + " ☆★\n");
				draft.append(Mail.URI_EXPERIENCES + "/" + experience.getId() + "\n\n");
			}
			break;
		case Mail.SUB_SURVEY:
			draft.append("アンケートへのご協力をお願いします！\n\n");
			draft.append("ただいまサービス向上のため、ダンサーズキャリアをご利用の皆様にアンケート調査を実施しております。\n5分程度で完了いたしますので、お時間のあるときにぜひご回答ください！\n\n");
			draft.append("▼アンケートはこちら▼\n");
			draft.append("https://goo.gl/forms/p3ax5dBmmPipB9J23\n\n");
			draft.append("今後の更なるサービス向上のため、1人でも多くの方の回答をお待ちしております！\n\n");
			break;
		default:
			break;
		}
		draft.append("\n↓マイページはこちら\n");
		draft.append(Mail.CONTEXT_PATH + "/user\n\n");
		draft.append("↓お問い合わせはこちらから\n");
		draft.append(Mail.CONTEXT_PATH + "/about/contact\n\n");
		draft.append("※こちらは「送信専用」です。ダンサーズキャリアへの問い合わせは上記アドレスからお願いいたします。");
		return draft.toString();
	}
}
