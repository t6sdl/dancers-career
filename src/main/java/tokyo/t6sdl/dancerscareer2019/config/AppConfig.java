package tokyo.t6sdl.dancerscareer2019.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@EnableAsync
@Configuration
public class AppConfig implements WebMvcConfigurer {
	private final MessageSource messageSource;
	
	public AppConfig(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource);
		return validator;
	}
	
	@Bean
	@Autowired
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
		
	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("com.mysql.jdbc.Driver");
		config.setJdbcUrl(System.getProperty("DB_URL_JDBC"));
		config.setUsername(System.getProperty("DB_USERNAME"));
		config.setPassword(System.getProperty("DB_PASSWORD"));
		config.setMaximumPoolSize(8);
		config.setMaxLifetime(50000);
		config.setLeakDetectionThreshold(30000);
		config.setConnectionInitSql("SET SESSION sql_mode='TRADITIONAL,NO_AUTO_VALUE_ON_ZERO,ONLY_FULL_GROUP_BY'");
		config.addDataSourceProperty("cachePrepStmts", true);
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("userServerPrepStmts", true);
		config.addDataSourceProperty("characterEncoding", "utf-8");
		HikariDataSource dataSource = new HikariDataSource(config);
		return dataSource;
	}
}