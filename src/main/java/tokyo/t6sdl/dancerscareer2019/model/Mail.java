package tokyo.t6sdl.dancerscareer2019.model;

import java.util.List;

import lombok.Data;

@Data
public class Mail {
	public static String CONTEXT_PATH = System.getProperty("DOMAIN", "http://localhost:8080");
	public static final String URI_VERIFY_EMAIL = Mail.CONTEXT_PATH + "/signup/verify-email?token=";
	public static final String URI_RESET_PWD = Mail.CONTEXT_PATH + "/signin/forget-pwd?token=";
	public static final String URI_EXPERIENCES = Mail.CONTEXT_PATH + "/experiences";
	public static final String URI_MAIL_SETTING = Mail.CONTEXT_PATH + "/user/account/change/mail-setting";
	public static final String NAME_OF_SUPPORT = "ダンサーズキャリア";
	public static final String TO_SUPPORT = "dancerscareer_support@t6sdl.tokyo";
	public static final String TO_ERROR = "dancerscareer_error@t6sdl.tokyo";
	public static final String SUB_WELCOME_TO_US = "ダンサーズキャリアへようこそ！";
	public static final String SUB_VERIFY_EMAIL = "メールアドレスの確認";
	public static final String SUB_RESET_PWD = "パスワードの再設定";
	public static final String SUB_CONTACT = "問い合わせ";
	public static final String SUB_REPLY_TO_CONTACT = "ダンサーズキャリアへお問い合わせいただきありがとうございます";
	public static final String SUB_NEW_ES = "新しいES/体験記が公開されました！";
	public static final String SUB_SURVEY = "アンケートへのご協力をお願いします！";
	
	private String to;
	private List<Account> accounts;
	private String subject;
	private String content;
	private String url;
	private List<Experience> experiences;
	
	public Mail(String to, String subject) {
		this.to = to;
		this.subject = subject;
	}
	
	public Mail(List<Account> accounts, String subject) {
		this.accounts = accounts;
		this.subject = subject;
	}
}
