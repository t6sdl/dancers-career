package tokyo.t6sdl.dancerscareer2019;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class DancersCareer2019Application {

	public static void main(String[] args) {
		Flyway flyway = new Flyway();
		flyway.setDataSource("jdbc:mysql://us-cdbr-iron-east-01.cleardb.net/heroku_6ed62eaddfd562d", "b23da6ad7dabc5", "f5f14f28");
		flyway.repair();
		flyway.clean();
		SpringApplication.run(DancersCareer2019Application.class, args);
	}
	
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/validation-errors");
		messageSource.setDefaultEncoding("utf-8");
		return messageSource;
	}
}
