package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.util.Date;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

public class JdbcLoginRepository extends JdbcTokenRepositoryImpl {
	private String updateTokenSql = JdbcTokenRepositoryImpl.DEF_UPDATE_TOKEN_SQL;
	
	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		getJdbcTemplate().update(updateTokenSql, tokenValue, lastUsed, series);
		getJdbcTemplate().update("UPDATE accounts SET last_login = CURRENT_TIMESTAMP WHERE EXISTS (SELECT 1 FROM persistent_logins WHERE persistent_logins.username = accounts.email AND series = ?", series);
	}
}
