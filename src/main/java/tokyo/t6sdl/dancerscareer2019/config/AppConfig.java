package tokyo.t6sdl.dancerscareer2019.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import tokyo.t6sdl.dancerscareer2019.io.LineNotifyManager;
import tokyo.t6sdl.dancerscareer2019.io.MailSender;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;

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
		config.setJdbcUrl("jdbc:mysql://us-cdbr-iron-east-01.cleardb.net/heroku_6ed62eaddfd562d");
		config.setUsername("b23da6ad7dabc5");
		config.setPassword("f5f14f28");
		config.setMaximumPoolSize(8);
		config.setLeakDetectionThreshold(5000);
		config.setConnectionInitSql("SET SESSION sql_mode='TRADITIONAL,NO_AUTO_VALUE_ON_ZERO,ONLY_FULL_GROUP_BY'");
		config.addDataSourceProperty("cachePrepStmts", true);
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("userServerPrepStmts", true);
		config.addDataSourceProperty("characterEncoding", "utf-8");
		HikariDataSource dataSource = new HikariDataSource(config);
		return dataSource;
	}
	
	@Bean
	@Autowired
	public MailSender mailSender(JavaMailSender javaMailSender, AccountRepository accountRepository, LineNotifyManager lineNotifyManager) {
		return new MailSender(javaMailSender, accountRepository, lineNotifyManager);
	}
}