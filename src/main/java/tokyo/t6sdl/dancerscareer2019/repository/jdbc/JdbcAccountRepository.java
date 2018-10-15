package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;

@Repository
public class JdbcAccountRepository implements AccountRepository {
	private final JdbcTemplate jdbcTemplate;
	private final String QUERIED_VALUE = "email, password, authority, valid_email, updated_at, created_at, last_login, email_token, password_token";
	
	public JdbcAccountRepository (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Account> find() {
		List<Account> results = jdbcTemplate.query(
				this.selectAccountIn("WHERE authority = 'ROLE_USER'", true), (resultSet, i) -> {
					Account account = new Account();
					this.adjustDataToAccount(account, resultSet);
					return account;
		});
		return results;
	}

	@Override
	public Account findOneByEmail(String email) {
		try {
			Account result = jdbcTemplate.queryForObject(
					this.selectAccountIn("WHERE email = ?", false), (resultSet, i) -> {
						Account account = new Account();
						this.adjustDataToAccount(account, resultSet);
						return account;
					}, email);
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Account findOneByEmailToken(String emailToken) {
		try {
			Account result = jdbcTemplate.queryForObject(
					this.selectAccountIn("WHERE authority = 'ROLE_USER' AND email_token = ?", false), (resultSet, i) -> {
						Account account = new Account();
						this.adjustDataToAccount(account, resultSet);
						return account;
					}, emailToken);
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Account findOneByPasswordToken(String passwordToken) {
		try {
			Account result = jdbcTemplate.queryForObject(
					this.selectAccountIn("WHERE authority = 'ROLE_USER' AND password_token = ?", false), (resultSet, i) -> {
						Account account = new Account();
						this.adjustDataToAccount(account, resultSet);
						return account;
					}, passwordToken);
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public String findEmailTokenByEmail(String email) {
		try {
			return jdbcTemplate.queryForObject("SELECT email_token FROM accounts WHERE email = ?", String.class, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public String findPasswordTokenByEmail(String email) {
		try {
			return jdbcTemplate.queryForObject("SELECT password_token FROM accounts WHERE email = ?", String.class, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public String findLineAccessTokenByEmail(String email) {
		try {
			return jdbcTemplate.queryForObject("SELECT line_access_token FROM accounts WHERE email = ?", String.class, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void insert(Account newAccount) {
		jdbcTemplate.update(
				"INSERT INTO accounts (email, password, updated_at, created_at, last_login) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
				newAccount.getEmail(), newAccount.getPassword());
		jdbcTemplate.update("UPDATE counts SET count = count + 1 WHERE name = 'accounts'");
	}

	@Override
	public void delete(String loggedInEmail) {
		jdbcTemplate.update(
				"DELETE FROM accounts WHERE email = ?",
				loggedInEmail);
		jdbcTemplate.update("UPDATE counts SET count = CASE WHEN count = 0 THEN 0 ELSE count - 1 END WHERE name = 'accounts'");
	}

	@Override
	public void updateEmail(String loggedInEmail, String newEmail) {
		jdbcTemplate.update(
				"UPDATE accounts SET email = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?",
				newEmail, loggedInEmail);
	}

	@Override
	public void updatePassword(String loggedInEmail, String newPassword) {
		jdbcTemplate.update(
				"UPDATE accounts SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?",
				newPassword, loggedInEmail);
	}

	@Override
	public void updateValidEmail(String loggedInEmail, boolean validEmail) {
		jdbcTemplate.update(
				"UPDATE accounts SET valid_email = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?",
				validEmail, loggedInEmail);
	}
	
	@Override
	public void updateLastLogin(String loggedInEmail) {
		jdbcTemplate.update(
				"UPDATE accounts SET last_login = CURRENT_TIMESTAMP WHERE email = ?",
				loggedInEmail);
	}
	
	@Override
	public void updateLineAccessToken(String loggedInEmail, String lineAccessToken) {
		jdbcTemplate.update(
				"UPDATE accounts SET line_access_token = ? WHERE email = ?",
				lineAccessToken, loggedInEmail);
	}

	@Override
	public void recordEmailToken(String loggedInEmail, String emailToken) {
		jdbcTemplate.update(
				"UPDATE accounts SET email_token = ? WHERE email = ?",
				emailToken, loggedInEmail);
	}

	@Override
	public void refreshEmailToken(String loggedInEmail) {
		jdbcTemplate.update(
				"UPDATE accounts SET email_token = NULL WHERE email = ?",
				loggedInEmail);
	}

	@Override
	public void recordPasswordToken(String loggedInEmail, String passwordToken) {
		jdbcTemplate.update(
				"UPDATE accounts SET password_token = ? WHERE email = ?",
				passwordToken, loggedInEmail);
	}

	@Override
	public void refreshPasswordToken(String loggedInEmail) {
		jdbcTemplate.update(
				"UPDATE accounts SET password_token = NULL WHERE email = ?",
				loggedInEmail);
	}
	
	private void adjustDataToAccount(Account account, ResultSet resultSet) throws SQLException {
		account.setEmail(resultSet.getString("email"));
		account.setPassword(resultSet.getString("password"));
		account.setAuthority(resultSet.getString("authority"));
		account.setValid_email(resultSet.getBoolean("valid_email"));
		Date updatedAt = resultSet.getTimestamp("updated_at");
		account.setUpdated_at(LocalDateTime.ofInstant(updatedAt.toInstant(), ZoneId.of("Asia/Tokyo")));
		Date createdAt = resultSet.getTimestamp("created_at");
		account.setCreated_at(LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.of("Asia/Tokyo")));
		Date lastLogin = resultSet.getTimestamp("last_login");
		account.setLast_login(LocalDateTime.ofInstant(lastLogin.toInstant(), ZoneId.of("Asia/Tokyo")));
		account.setEmail_token(resultSet.getString("email_token"));
		account.setPassword_token(resultSet.getString("password_token"));
	}
	
	private String selectAccountIn(String conditions, boolean multiple) {
		if (multiple) {
			return "SELECT " + this.QUERIED_VALUE + " FROM accounts " + conditions + " ORDER BY last_login DESC";
		} else {
			return "SELECT " + this.QUERIED_VALUE + " FROM accounts " + conditions;
		}
	}
}