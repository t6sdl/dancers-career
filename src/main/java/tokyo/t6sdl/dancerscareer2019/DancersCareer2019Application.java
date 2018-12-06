package tokyo.t6sdl.dancerscareer2019;

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
//		if (System.getenv("SPRING_PROFILES_ACTIVE").equals("staging")) {
//			Flyway flyway = new Flyway();
//			flyway.setDataSource(System.getenv("DB_URL_JDBC"), System.getenv("DB_USERNAME"), System.getenv("DB_PASSWORD"));
//			flyway.repair();
//			flyway.clean();			
//		}
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
