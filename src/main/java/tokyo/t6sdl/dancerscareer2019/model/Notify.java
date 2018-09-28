package tokyo.t6sdl.dancerscareer2019.model;

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
