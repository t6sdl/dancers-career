package tokyo.t6sdl.dancerscareer2019;

import java.net.URI;
import java.net.URISyntaxException;
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

	public static void main(String[] args) throws URISyntaxException {
//		if (System.getenv("SPRING_PROFILES_ACTIVE").equals("staging")) {
//			Flyway flyway = new Flyway();
//			flyway.setDataSource(System.getenv("DB_URL_JDBC"), System.getenv("DB_USERNAME"), System.getenv("DB_PASSWORD"));
//			flyway.repair();
//			flyway.clean();			
//		}
		if (System.getProperty("env", "development").equals("development")) {
			ResourceBundle rb = ResourceBundle.getBundle("application-development");
			System.setProperty("db.url.jdbc", rb.getString("db.url.jdbc"));
			System.setProperty("db.username", rb.getString("db.username"));
			System.setProperty("db.password", rb.getString("db.password"));
		} else {
			URI uri = new URI(System.getenv("CLEARDB_DATABASE_URL"));
			String url = "jdbc:mysql://" + uri.getHost() + uri.getPath();
			String username = uri.getUserInfo().split(":")[0];
			String password = uri.getUserInfo().split(":")[1];
			System.setProperty("db.url.jdbc", url);
			System.setProperty("db.username", username);
			System.setProperty("db.password", password);
			System.setProperty("domain", System.getenv("DOMAIN"));
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
