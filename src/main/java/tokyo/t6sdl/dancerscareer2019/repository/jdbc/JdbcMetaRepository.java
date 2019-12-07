package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.repository.MetaRepository;

@RequiredArgsConstructor
@Repository
public class JdbcMetaRepository implements MetaRepository {
	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<String> tables() {
		return Arrays.asList(jdbcTemplate.query("SHOW TABLES;", (resultSet, i) -> {
			return resultSet.getString(0);
		}));
	}

	@Override
	public List<String> columns(String table) {
		return Arrays.asList(jdbcTemplate.query("SHOW COLUMNS FROM " + table, (resultSet, i) -> {
			return resultSet.getString("Field");
		}));
	}

}
