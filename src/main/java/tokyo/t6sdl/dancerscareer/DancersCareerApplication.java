package tokyo.t6sdl.dancerscareer;

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
public class DancersCareerApplication {

	public static void main(String[] args) throws URISyntaxException {
		// if (System.getenv("SPRING_PROFILES_ACTIVE").equals("staging")) {
		// 	Flyway flyway = new Flyway();
		// 	flyway.setDataSource(System.getenv("DB_URL_JDBC"), System.getenv("DB_USERNAME"), System.getenv("DB_PASSWORD"));
		// 	flyway.repair();
		// 	flyway.clean();
		// }
		setDBConnectionInfo();
		ResourceBundle bundle = environmentBundle();
		System.setProperty("domain", bundle.getString("domain"));
		System.setProperty("spring.datasource.hikari.maximum-pool-size", bundle.getString("spring.datasource.hikari.maximum-pool-size"));
		SpringApplication.run(DancersCareerApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/validation-errors");
		messageSource.setDefaultEncoding("utf-8");
		return messageSource;
	}

	private static void setDBConnectionInfo() throws URISyntaxException {
		String url = System.getenv("DB_URL");
		String username = System.getenv("DB_USERNAME");
		String password = System.getenv("DB_PASSWORD");
		System.setProperty("db.url.jdbc", url);
		System.setProperty("db.username", username);
		System.setProperty("db.password", password);
	}

	private static ResourceBundle environmentBundle() {
		String env = System.getProperty("env", "development");
		return ResourceBundle.getBundle("application-" + env);
	}
}
