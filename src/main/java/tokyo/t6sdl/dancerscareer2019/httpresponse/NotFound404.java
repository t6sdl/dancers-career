package tokyo.t6sdl.dancerscareer2019.httpresponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("serial")
public class NotFound404 extends RuntimeException {
	
}