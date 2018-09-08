package tokyo.t6sdl.dancerscareer2019.model;

import lombok.Data;

@Data
public class Mail {
	public static final String CONTEXT_PATH = "http://localhost:8080";
	public static final String URI_VERIFY_EMAIL = Mail.CONTEXT_PATH + "/signup/verify-email?token=";
	public static final String URI_RESET_PWD = Mail.CONTEXT_PATH + "/signin/forget-pwd?token=";
	public static final String NAME_OF_SUPPORT = "（仮）ダンサーズキャリア事務局";
	public static final String TO_SUPPORT = "dancerscareer_support@t6sdl.tokyo";
	public static final String TO_ERROR = "dancerscareer_error@t6sdl.tokyo";
	public static final String SUB_VERIFY_EMAIL = "メールアドレスの確認";
	public static final String SUB_RESET_PWD = "パスワードの再設定";
	public static final String SUB_CONTACT = "問い合わせ";
	public static final String SUB_REPLY_TO_CONTACT = "ダンサーズキャリアへお問い合わせいただきありがとうございます";
}
