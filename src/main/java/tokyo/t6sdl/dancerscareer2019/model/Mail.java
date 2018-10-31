package tokyo.t6sdl.dancerscareer2019.model;

import lombok.Data;

@Data
public class Mail {
	public static String CONTEXT_PATH = System.getenv("DOMAIN");
	public static final String URI_VERIFY_EMAIL = Mail.CONTEXT_PATH + "/signup/verify-email?token=";
	public static final String URI_RESET_PWD = Mail.CONTEXT_PATH + "/signin/forget-pwd?token=";
	public static final String NAME_OF_SUPPORT = "ダンサーズキャリア";
	public static final String TO_SUPPORT = "dancerscareer_support@t6sdl.tokyo";
	public static final String TO_ERROR = "dancerscareer_error@t6sdl.tokyo";
	public static final String SUB_WELCOME_TO_US = "ダンサーズキャリアへようこそ！";
	public static final String SUB_VERIFY_EMAIL = "メールアドレスの確認";
	public static final String SUB_RESET_PWD = "パスワードの再設定";
	public static final String SUB_CONTACT = "問い合わせ";
	public static final String SUB_REPLY_TO_CONTACT = "ダンサーズキャリアへお問い合わせいただきありがとうございます";
	
	private String to;
	private String subject;
	private String content;
	private String url;
	
	public Mail(String to, String subject) {
		this.to = to;
		this.subject = subject;
	}
}
