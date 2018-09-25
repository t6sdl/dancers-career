package tokyo.t6sdl.dancerscareer2019.model;

import java.lang.reflect.Field;

import org.springframework.web.util.UriComponentsBuilder;

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
	
	public String toUriString() {
		UriComponentsBuilder uri = UriComponentsBuilder.newInstance();
		for (Field field: this.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				uri.queryParam(field.getName(), field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return null;
			}
		}
		StringBuilder str = new StringBuilder();
		str.append(uri.build().encode().toUriString());
		str.deleteCharAt(str.indexOf("?"));
		return str.toString();
	}
}
