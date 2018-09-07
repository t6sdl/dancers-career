package tokyo.t6sdl.dancerscareer2019.model;

import lombok.Data;

@Data
public class Mail {
	public static final String CONTEXT_PATH = "http://localhost:8080";
	public static final String SUB_VERIFY_EMAIL = "メールアドレスの確認";
	public static final String SUB_RESET_PWD = "パスワードの再設定";
}
