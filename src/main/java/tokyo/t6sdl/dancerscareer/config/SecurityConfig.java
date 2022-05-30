package tokyo.t6sdl.dancerscareer.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.repository.jdbc.JdbcLoginRepository;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final DataSource dataSource;
	private SigninSuccessHandler signinSuccessHandler;
	private SignoutSuccessHandler signoutSuccessHandler;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico", "/css/**", "/img/**", "/js/**", "/mails/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/signup/profile").authenticated()
			.antMatchers("/", "/signin/**", "/signup/**", "/about/**", "/experiences/**", "/mentors/**", "/news/**", "/user/account/change/mail-setting").permitAll()
			.antMatchers("/admin/**").hasRole("ADMIN")
			.antMatchers("/user/**").hasRole("USER")
			.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginProcessingUrl("/login")
			.loginPage("/signin")
			.successHandler(signinSuccessHandler)
			.failureUrl("/signin?error")
			.usernameParameter("email").passwordParameter("password")
		.and()
			.rememberMe()
			.rememberMeParameter("remember-me")
			.useSecureCookie(true)
			.tokenRepository(createTokenRepository())
		.and()
			.csrf().csrfTokenRepository(new CookieCsrfTokenRepository())
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
			.logoutSuccessHandler(signoutSuccessHandler)
			.deleteCookies("JSESSIONID");
	}

	@Bean
	public PersistentTokenRepository createTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcLoginRepository();
		db.setDataSource(dataSource);
		return db;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager auth() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void setSigninSuccessHandler(SigninSuccessHandler signinSuccessHandler) {
		this.signinSuccessHandler = signinSuccessHandler;
	}

	@Autowired
	public void setSignoutSuccessHandler(SignoutSuccessHandler signoutSuccessHandler) {
		this.signoutSuccessHandler = signoutSuccessHandler;
	}
}
