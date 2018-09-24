package tokyo.t6sdl.dancerscareer2019.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ParamForLineOAuth {
	private final String grant_type;
	private final String code;
	private final String redirect_uri;
	private final String client_id;
	private final String client_secret;
}
