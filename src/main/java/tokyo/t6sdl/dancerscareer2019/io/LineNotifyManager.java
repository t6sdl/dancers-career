package tokyo.t6sdl.dancerscareer2019.io;

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
import tokyo.t6sdl.dancerscareer2019.model.AccessToken;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.model.Notify;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

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
		StringBuilder draft = new StringBuilder();
		draft.append("\n");
		switch (mail.getSubject()) {
		case Mail.SUB_WELCOME_TO_US:
			draft.append("ダンサーズキャリアにご登録いただきありがとうございます！\n");
			draft.append("今後はダンサーズキャリアからのメールが届くと、LINEへもメッセージが届きます！\n\n");
			if (!(securityService.findLoggedInValidEmail())) {
				draft.append("↓下記のURLからメールアドレスの確認をお済ませください。\n");
				draft.append(Mail.URI_VERIFY_EMAIL + accountService.getEmailTokenByEmail(mail.getTo()) + "\n\n");
			}
			break;
		case Mail.SUB_VERIFY_EMAIL:
			draft.append("↓下記のURLからメールアドレスの確認をお済ませください。\n\n");
			draft.append(Mail.URI_VERIFY_EMAIL + accountService.getEmailTokenByEmail(mail.getTo()) + "\n\n");
		case Mail.SUB_RESET_PWD:
			draft.append("↓下記のURLからパスワードの再設定ができます。\nURLの有効期限は30分です。\n\n");
			draft.append(Mail.URI_VERIFY_EMAIL + accountService.getPasswordTokenByEmail(mail.getTo()) + "\n\n");
		case Mail.SUB_REPLY_TO_CONTACT:
			draft.append("お問い合わせいただきありがとうございます。\nお返事に数日程度かかる場合もございます。ご了承ください。\n\n");
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
