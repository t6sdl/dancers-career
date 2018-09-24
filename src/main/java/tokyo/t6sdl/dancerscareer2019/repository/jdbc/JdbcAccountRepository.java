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
	
	public JdbcAccountRepository (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Account> find() {
		List<Account> results = jdbcTemplate.query(
				"SELECT * FROM accounts ORDER BY created_at ASC", (resultSet, i) -> {
					Account account = new Account();
					this.adjustDataToAccount(account, resultSet);
					return account;
		});
		this.setLastLogin(results);
		return results;
	}

	@Override
	public Account findOneByEmail(String email) {
		try {
			Account result = jdbcTemplate.queryForObject(
					"SELECT * FROM accounts WHERE email=?", (resultSet, i) -> {
						Account account = new Account();
						this.adjustDataToAccount(account, resultSet);
						return account;
					}, email);
			this.setLastLogin(result);
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Account findOneByEmailToken(String emailToken) {
		try {
			Account result = jdbcTemplate.queryForObject(
					"SELECT * FROM accounts WHERE email_token = ?", (resultSet, i) -> {
						Account account = new Account();
						this.adjustDataToAccount(account, resultSet);
						return account;
					}, emailToken);
			this.setLastLogin(result);
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Account findOneByPasswordToken(String passwordToken) {
		try {
			Account result = jdbcTemplate.queryForObject(
					"SELECT * FROM accounts WHERE password_token = ?", (resultSet, i) -> {
						Account account = new Account();
						this.adjustDataToAccount(account, resultSet);
						return account;
					}, passwordToken);
			this.setLastLogin(result);
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void insert(Account newAccount) {
		jdbcTemplate.update(
				"INSERT INTO accounts (email, password, updated_at, created_at, last_login) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
				newAccount.getEmail(), newAccount.getPassword());
	}

	@Override
	public void delete(String loggedInEmail) {
		jdbcTemplate.update(
				"DELETE FROM accounts WHERE email = ?",
				loggedInEmail);
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
	
	public void updateLastLogin(String loggedInEmail) {
		jdbcTemplate.update(
				"UPDATE accounts SET last_login = CURRENT_TIMESTAMP WHERE email = ?",
				loggedInEmail);
	}

	@Override
	public void recordEmailToken(String loggedInEmail, String emailToken) {
		jdbcTemplate.update(
				"UPDATE accounts SET email_token = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?",
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
				"UPDATE accounts SET password_token = ?, updated_at = CURRENT_TIMESTAMP WHERE email = ?",
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
		Date lastLogin = resultSet.getTimestamp("last_login");
		account.setLast_login(LocalDateTime.ofInstant(lastLogin.toInstant(), ZoneId.of("Asia/Tokyo")));
		Date updatedAt = resultSet.getTimestamp("updated_at");
		account.setUpdated_at(LocalDateTime.ofInstant(updatedAt.toInstant(), ZoneId.of("Asia/Tokyo")));
		Date createdAt = resultSet.getTimestamp("created_at");
		account.setCreated_at(LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.of("Asia/Tokyo")));
		account.setEmail_token(resultSet.getString("email_token"));
		account.setPassword_token(resultSet.getString("password_token"));
	}
	
	private void setLastLogin(List<Account> accounts) {
		accounts.forEach(account -> {
			List<LocalDateTime> lastLogins = jdbcTemplate.query(
					"SELECT last_used FROM persistent_logins WHERE username = ? ORDER BY last_used DESC LIMIT 1", (resultSet, i) -> {
						Date lastUsed = resultSet.getTimestamp("last_used");
						LocalDateTime lastLogin = LocalDateTime.ofInstant(lastUsed.toInstant(), ZoneId.of("Asia/Tokyo"));
						return lastLogin;
					}, account.getEmail());
			if (!(lastLogins.isEmpty()) && lastLogins.get(0).isAfter(account.getLast_login())) {
				account.setLast_login(lastLogins.get(0));
			}
		});
	}
	
	private void setLastLogin(Account account) {
		List<LocalDateTime> lastLogins = jdbcTemplate.query(
				"SELECT last_used FROM persistent_logins WHERE username = ? ORDER BY last_used DESC LIMIT 1", (resultSet, i) -> {
					Date lastUsed = resultSet.getTimestamp("last_used");
					LocalDateTime lastLogin = LocalDateTime.ofInstant(lastUsed.toInstant(), ZoneId.of("Asia/Tokyo"));
					return lastLogin;
				}, account.getEmail());
		if (!(lastLogins.isEmpty()) && lastLogins.get(0).isAfter(account.getLast_login())) {
			account.setLast_login(lastLogins.get(0));
		}
	}
}