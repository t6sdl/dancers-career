package tokyo.t6sdl.dancerscareer.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.repository.AccountRepository;

@RequiredArgsConstructor
@Repository
public class JdbcAccountRepository implements AccountRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<Account> find() {
		String sql = "SELECT * FROM accounts WHERE authority = 'ROLE_USER' ORDER BY last_login DESC";
		return jdbcTemplate.query(sql, (resultSet, i) -> {
				Account account = new Account();
				return this.adjustToAccount(account, resultSet);
			});
	}

	@Override
	public Account findOneByEmail(String email) {
		List<String> target = Arrays.asList("email = :em");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", email);
		return this.findAccount(target, params);
	}

	@Override
	public Account findOneByEmailToken(String emailToken) {
		List<String> target = Arrays.asList("authority = 'ROLE_USER'", "email_token = :eT");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eT", emailToken);
		return this.findAccount(target, params);
	}

	@Override
	public Account findOneByPasswordToken(String passwordToken) {
		List<String> target = Arrays.asList("authority = 'ROLE_USER'", "password_token = :pT");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pT", passwordToken);
		return this.findAccount(target, params);
	}

	@Override
	public List<Account> findForMassMailBy(int by) {
		String sql;
		switch (by) {
		case 0:
			sql = "SELECT email, line_access_token FROM accounts WHERE authority = 'ROLE_USER'";
			break;
		case 1:
			sql = "SELECT email, line_access_token FROM accounts WHERE new_es_mail = true";
			break;
		default:
			return null;
		}
		return jdbcTemplate.query(sql, (resultSet, i) -> {
			Account account = new Account();
			account.setEmail(resultSet.getString("email"));
			account.setLineAccessToken(resultSet.getString("line_access_token"));
			return account;
		});
	}

	@Override
	public String findEmailTokenByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", email);
		try {
			return jdbcTemplate.queryForObject("SELECT email_token FROM accounts WHERE email = :em", params, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public String findPasswordTokenByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", email);
		try {
			return jdbcTemplate.queryForObject("SELECT password_token FROM accounts WHERE email = :em", params, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public String findLineAccessTokenByEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", email);
		try {
			return jdbcTemplate.queryForObject("SELECT line_access_token FROM accounts WHERE email = :em", params, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void insert(Account newAccount) {
		String sql = "INSERT INTO accounts VALUES (:em, :pw, 'ROLE_USER', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, true)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", newAccount.getEmail());
		params.put("pw", newAccount.getPassword());
		jdbcTemplate.update(sql, params);
		jdbcTemplate.getJdbcTemplate().update("UPDATE counts SET count = count + 1 WHERE name = 'accounts'");
	}

	@Override
	public void delete(String loggedInEmail) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", loggedInEmail);
		jdbcTemplate.update("DELETE FROM accounts WHERE email = :em", params);
		jdbcTemplate.getJdbcTemplate().update("UPDATE counts SET count = CASE WHEN count = 0 THEN 0 ELSE count - 1 END WHERE name = 'accounts'");
	}

	@Override
	public void updateEmail(String loggedInEmail, String newEmail) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newEm", newEmail);
		params.put("oldEm", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET email = :newEm, updated_at = CURRENT_TIMESTAMP WHERE email = :oldEm", params);
	}

	@Override
	public void updatePassword(String loggedInEmail, String newPassword) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pw", newPassword);
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET password = :pw, updated_at = CURRENT_TIMESTAMP WHERE email = :em", params);
	}

	@Override
	public void updateValidEmail(String loggedInEmail, boolean validEmail) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vE", validEmail);
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET valid_email = :vE, updated_at = CURRENT_TIMESTAMP WHERE email = :em", params);
	}

	@Override
	public void updateLastLogin(String loggedInEmail) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET last_login = CURRENT_TIMESTAMP WHERE email = :em", params);
	}

	@Override
	public void updateLineAccessToken(String loggedInEmail, String lineAccessToken) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lAT", lineAccessToken);
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET line_access_token = :lAT, updated_at = CURRENT_TIMESTAMP WHERE email = :em", params);
	}

	@Override
	public void updateNewEsMail(String loggedInEmail, boolean esUpdateNotification) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nEM", esUpdateNotification);
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET new_es_mail = :nEM WHERE email = :em", params);
	}

	@Override
	public void recordEmailToken(String loggedInEmail, String emailToken) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eT", emailToken);
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET email_token = :eT, updated_at = CURRENT_TIMESTAMP WHERE email = :em", params);
	}

	@Override
	public void refreshEmailToken(String loggedInEmail) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET email_token = NULL WHERE email = :em", params);
	}

	@Override
	public void recordPasswordToken(String loggedInEmail, String passwordToken) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pT", passwordToken);
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET password_token = :pT, updated_at = CURRENT_TIMESTAMP WHERE email = :em", params);
	}

	@Override
	public void refreshPasswordToken(String loggedInEmail) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("em", loggedInEmail);
		jdbcTemplate.update("UPDATE accounts SET password_token = NULL WHERE email = :em", params);
	}

	private Account adjustToAccount(Account account, ResultSet resultSet) throws SQLException {
		account.setEmail(resultSet.getString("email"));
		account.setPassword(resultSet.getString("password"));
		account.setAuthority(resultSet.getString("authority"));
		account.setValidEmail(resultSet.getBoolean("valid_email"));
		Date updatedAt = resultSet.getTimestamp("updated_at");
		account.setUpdatedAt(LocalDateTime.ofInstant(updatedAt.toInstant(), ZoneId.of("Asia/Tokyo")));
		Date createdAt = resultSet.getTimestamp("created_at");
		account.setCreatedAt(LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.of("Asia/Tokyo")));
		Date lastLogin = resultSet.getTimestamp("last_login");
		account.setLastLogin(LocalDateTime.ofInstant(lastLogin.toInstant(), ZoneId.of("Asia/Tokyo")));
		account.setEmailToken(resultSet.getString("email_token"));
		account.setPasswordToken(resultSet.getString("password_token"));
		account.setLineAccessToken(resultSet.getString("line_access_token"));
		account.setEsUpdateNotification(resultSet.getBoolean("new_es_mail"));
		return account;
	}

	private Account findAccount(List<String> target, Map<String, Object> params) {
		List<String> elements = new LinkedList<String>(Arrays.asList("SELECT * FROM accounts"));
		if (!Objects.equals(target, null) && !target.isEmpty()) elements.addAll(Arrays.asList("WHERE", target.stream().collect(Collectors.joining(" AND "))));
		try {
			String sql = elements.stream().collect(Collectors.joining(" "));
			return jdbcTemplate.queryForObject(sql, params, (resultSet, i) -> {
				Account account = new Account();
				return this.adjustToAccount(account, resultSet);
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
