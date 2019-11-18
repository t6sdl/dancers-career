package tokyo.t6sdl.dancerscareer2019;

import java.util.ResourceBundle;

//import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DancersCareer2019Application {

	public static void main(String[] args) {
//		if (System.getProperty("SPRING_PROFILES_ACTIVE", "staging").equals("staging")) {
//			Flyway flyway = new Flyway();
//			flyway.setDataSource(System.getenv("DB_URL_JDBC"), System.getenv("DB_USERNAME"), System.getenv("DB_PASSWORD"));
//			flyway.repair();
//			flyway.clean();			
//		}
		System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
		System.out.println(System.getProperty("SPRING_PROFILES_ACTIVE"));
		System.out.println(System.getenv("SPRING_PROFILES_ACTIVE"));
		if (System.getProperty("SPRING_PROFILES_ACTIVE", "development").equals("development")) {
			System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
			ResourceBundle rb = ResourceBundle.getBundle("application-development");
			System.setProperty("DB_URL_JDBC", rb.getString("db.url.jdbc"));
			System.setProperty("DB_USERNAME", rb.getString("db.username"));
			System.setProperty("DB_PASSWORD", rb.getString("db.password"));
		}
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
