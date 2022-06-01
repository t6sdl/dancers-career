package tokyo.t6sdl.dancerscareer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Notify {
	@JsonProperty("status")
	private int status;
	@JsonProperty("message")
	private String message;
	@JsonProperty("targetType")
	private String targetType;
	@JsonProperty("target")
	private String target;
}
