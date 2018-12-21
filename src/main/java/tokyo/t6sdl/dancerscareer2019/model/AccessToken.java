package tokyo.t6sdl.dancerscareer2019.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessToken {
	@JsonProperty("access_token")
	private String access_token;
	private String email;
}
