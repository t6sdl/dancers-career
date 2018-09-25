package tokyo.t6sdl.dancerscareer2019.model;

import java.lang.reflect.Field;

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
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Field field: this.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				str.append(field.getName()).append("=").append(field.get(this)).append("&");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				return null;
			}
		}
		str.deleteCharAt(str.lastIndexOf("&"));
		return str.toString();
	}
}
