package tokyo.t6sdl.dancerscareer.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
import tokyo.t6sdl.dancerscareer.model.Profile;
import tokyo.t6sdl.dancerscareer.model.Student;
import tokyo.t6sdl.dancerscareer.repository.ProfileRepository;

@RequiredArgsConstructor
@Repository
public class JdbcProfileRepository implements ProfileRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final List<String> COLUMNS = new ArrayList<String>(Arrays.asList("accounts.email", "valid_email", "last_login", "family_name", "given_name", "kana_family_name", "kana_given_name", "date_of_birth", "sex", "phone", "major", "univ_loc", "univ_name", "univ_fac", "univ_dep", "grad_loc", "grad_name", "grad_school", "grad_div", "graduated_in", "degree", "club", "position", "likes"));
	private final List<List<String>> SORT_LIST = Arrays.asList(Arrays.asList("last_login DESC"), Arrays.asList("kana_family_name ASC", "kana_given_name ASC", "last_login DESC"), Arrays.asList("univ_loc ASC", "univ_name ASC", "univ_fac ASC", "univ_dep ASC", "last_login DESC"));

	@Override
	public Profile findOneByEmail(String email) {
		 try {
			 Map<String, Object> params = new HashMap<String, Object>();
			 params.put("email", email);
			 return jdbcTemplate.queryForObject("SELECT * FROM profiles WHERE email = :email", params, (resultSet, i) -> {
					Profile profile = new Profile();
					return this.adjustToProf(profile, resultSet);
				});
		 } catch (EmptyResultDataAccessException e) {
			 return null;
		 }
	}

	@Override
	public Map<String, Object> find(int sort) {
		try {
			Integer count = jdbcTemplate.getJdbcTemplate().queryForObject("SELECT COUNT(email) FROM accounts WHERE authority = 'ROLE_USER'", Integer.class);
			List<String> order = this.SORT_LIST.get(sort);
			String sql = "SELECT " + this.COLUMNS.stream().collect(Collectors.joining(", ")) + " "
					+ "FROM accounts LEFT OUTER JOIN profiles "
					+ "ON accounts.email = profiles.email "
					+ "WHERE authority = 'ROLE_USER' "
					+ "ORDER BY " + order.stream().collect(Collectors.joining(", "));
			List<Student> students = jdbcTemplate.query(sql, (resultSet, i) -> {
					Student student = new Student();
					return this.adjustToStu(student, resultSet);
				});
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("count", count);
			result.put("students", students);
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByName(int sort, String kanaFamilyName, String kanaGivenName) {
		try {
			List<String> target = Arrays.asList("kana_family_name = :kFN", "kana_given_name = :kGN");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("kFN", kanaFamilyName);
			params.put("kGN", kanaGivenName);
			return this.findStudents(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByFamilyName(int sort, String kanaFamilyName) {
		try {
			List<String> target = Arrays.asList("kana_family_name = :kFN");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("kFN", kanaFamilyName);
			return this.findStudents(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByUnivLoc(int sort, String univLoc) {
		try {
			List<String> target = Arrays.asList("univ_loc = :uL");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uL", univLoc);
			return this.findStudents(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByUnivName(int sort, String univLoc, String univName) {
		try {
			List<String> target = Arrays.asList("univ_loc = :uL", "univ_name = :uN");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uL", univLoc);
			params.put("uN", univName);
			return this.findStudents(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByUnivFac(int sort, String univLoc, String univName, String univFac) {
		try {
			List<String> target = Arrays.asList("univ_loc = :uL", "univ_name = :uN", "univ_fac = :uF");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uL", univLoc);
			params.put("uN", univName);
			params.put("uF", univFac);
			return this.findStudents(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByUnivDep(int sort, String univLoc, String univName, String univFac, String univDep) {
		try {
			List<String> target = Arrays.asList("univ_loc = :uL", "univ_name = :uN", "univ_fac = :uF", "univ_dep = :uD");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("uL", univLoc);
			params.put("uN", univName);
			params.put("uF", univFac);
			params.put("uD", univDep);
			return this.findStudents(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pos", position);
		List<String> emails;
		if (andSearch) {
			String sql = "SELECT email FROM positions WHERE position IN (:pos) GROUP BY email HAVING COUNT(email) = :size ORDER BY email DESC";
			params.put("size", position.size());
			emails = jdbcTemplate.queryForList(sql, params, String.class);
		} else {
			String sql = "SELECT email FROM positions WHERE position IN (:pos) GROUP BY email ORDER BY email DESC";
			emails = jdbcTemplate.queryForList(sql, params, String.class);
		}
		try {
			List<String> target = Arrays.asList(!emails.isEmpty() ? "profiles.email IN (:emails)" : "profiles.email = NULL");
			List<String> order = this.SORT_LIST.get(sort);
			params.clear();
			params.put("emails", emails);
			return this.findStudents(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public String findFamilyNameByEmail(String email) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("email", email);
			return jdbcTemplate.queryForObject("SELECT family_name FROM profiles WHERE email = :email", params, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<String> findLikesByEmail(String email) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("email", email);
			String likes = jdbcTemplate.queryForObject("SELECT likes FROM profiles WHERE email = :email", params, String.class);
			return new ArrayList<String>(Arrays.asList(likes.split(",")));
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<String>(Arrays.asList(""));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insert(Profile newProf) {
		String sql = "INSERT INTO profiles VALUES (:email, :fN, :giN, :kFN, :kGN, :bth, :sex, :ph, :mjr, :uL, :uN, :uF, :uD, :gL, :grN, :gS, :gD, :gI, :deg, :clb, :pos, '')";
		Date birth = Date.from(newProf.getBirth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", newProf.getEmail());
		params.put("fN", newProf.getFamilyName());
		params.put("giN", newProf.getGivenName());
		params.put("kFN", newProf.getKanaFamilyName());
		params.put("kGN", newProf.getKanaGivenName());
		params.put("bth", birth);
		params.put("sex", newProf.getSex());
		params.put("ph", newProf.getPhone());
		params.put("mjr", newProf.getMajor());
		params.put("uL", newProf.getUnivLoc());
		params.put("uN", newProf.getUnivName());
		params.put("uF", newProf.getUnivFac());
		params.put("uD", newProf.getUnivDep());
		params.put("gL", newProf.getGradLoc());
		params.put("grN", newProf.getGradName());
		params.put("gS", newProf.getGradSchool());
		params.put("gD", newProf.getGradDiv());
		params.put("gI", newProf.getGraduatedIn());
		params.put("deg", newProf.getDegree());
		params.put("clb", newProf.getClub());
		params.put("pos", newProf.getPosition().stream().collect(Collectors.joining(",")));
		jdbcTemplate.update(sql, params);
		if (!newProf.getPosition().contains("")) {
			List<Map<String, Object>> paramMaps = new ArrayList<Map<String, Object>>();
			for (String p : newProf.getPosition()) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("position", p);
				paramMap.put("email", newProf.getEmail());
				paramMaps.add(paramMap);
			}
			jdbcTemplate.batchUpdate("INSERT INTO positions VALUES (:position, :email)", paramMaps.toArray(new Map[paramMaps.size()]));
		}
		jdbcTemplate.getJdbcTemplate().update("UPDATE counts SET count = count + 1 WHERE name = 'profiles'");
	}

	@Override
	public void delete(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		jdbcTemplate.update("DELETE FROM positions WHERE email = :email", params);
		jdbcTemplate.update("DELETE FROM profiles WHERE email = :email", params);
		jdbcTemplate.getJdbcTemplate().update("UPDATE counts SET count = CASE WHEN count = 0 THEN 0 ELSE count - 1 END WHERE name = 'profiles'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Profile prof) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", prof.getEmail());
		List<String> oldPos = jdbcTemplate.queryForList("SELECT position FROM positions WHERE email = :email", params, String.class);
		List<String> newPos = prof.getPosition();
		Date birth = Date.from(prof.getBirth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		params.put("fN", prof.getFamilyName());
		params.put("giN", prof.getGivenName());
		params.put("kFN", prof.getKanaFamilyName());
		params.put("kGN", prof.getKanaGivenName());
		params.put("bth", birth);
		params.put("sex", prof.getSex());
		params.put("ph", prof.getPhone());
		params.put("mjr", prof.getMajor());
		params.put("uL", prof.getUnivLoc());
		params.put("uN", prof.getUnivName());
		params.put("uF", prof.getUnivFac());
		params.put("uD", prof.getUnivDep());
		params.put("gL", prof.getGradLoc());
		params.put("grN", prof.getGradName());
		params.put("gS", prof.getGradSchool());
		params.put("gD", prof.getGradDiv());
		params.put("gI", prof.getGraduatedIn());
		params.put("deg", prof.getDegree());
		params.put("clb", prof.getClub());
		params.put("pos", newPos.stream().collect(Collectors.joining(",")));
		String sql = "UPDATE profiles SET "
				+ "family_name = :fN, "
				+ "given_name = :giN, "
				+ "kana_family_name = :kFN, "
				+ "kana_given_name = :kGN, "
				+ "date_of_birth = :bth, "
				+ "sex = :sex, "
				+ "phone = :ph, "
				+ "major = :mjr, "
				+ "univ_loc = :uL, "
				+ "univ_name = :uN, "
				+ "univ_fac = :uF, "
				+ "univ_dep = :uD, "
				+ "grad_loc = :gL, "
				+ "grad_name = :grN, "
				+ "grad_school = :gS, "
				+ "grad_div = :gD, "
				+ "graduated_in = :gI, "
				+ "degree = :deg, "
				+ "club = :clb, "
				+ "position = :pos "
				+ "WHERE email = :email";
		jdbcTemplate.update(sql, params);
		List<String> duplication = new ArrayList<String>();
		oldPos.forEach(pos -> {
			if (newPos.contains(pos)) duplication.add(pos);
		});
		oldPos.removeAll(duplication);
		newPos.removeAll(duplication);
		if (!oldPos.isEmpty()) {
			params.clear();
			params.put("email", prof.getEmail());
			params.put("oldPos", oldPos);
			jdbcTemplate.update("DELETE FROM positions WHERE position IN (:oldPos) AND email = :email", params);
		}
		if (!(newPos.isEmpty())) {
			List<Map<String, Object>> paramMaps = new ArrayList<Map<String, Object>>();
			for (String p : newPos) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("position", p);
				paramMap.put("email", prof.getEmail());
				paramMaps.add(paramMap);
			}
			jdbcTemplate.batchUpdate("INSERT INTO positions VALUES (:position, :email)", paramMaps.toArray(new Map[paramMaps.size()]));
		}
	}

	@Override
	public void updateLikes(String email, List<String> likes) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("likes", likes.stream().collect(Collectors.joining(",")));
		jdbcTemplate.update("UPDATE profiles SET likes = :likes WHERE email = :email", params);
	}

	private Profile adjustToProf(Profile profile, ResultSet resultSet) throws SQLException {
		profile.setEmail(resultSet.getString("email"));
		profile.setFamilyName(resultSet.getString("family_name"));
		profile.setGivenName(resultSet.getString("given_name"));
		profile.setKanaFamilyName(resultSet.getString("kana_family_name"));
		profile.setKanaGivenName(resultSet.getString("kana_given_name"));
		Date birth = resultSet.getTimestamp("date_of_birth");
		profile.setBirth(LocalDate.ofInstant(birth.toInstant(), ZoneId.systemDefault()));
		profile.setSex(resultSet.getString("sex"));
		profile.setPhone(resultSet.getString("phone"));
		profile.setMajor(resultSet.getString("major"));
		profile.setUnivLoc(resultSet.getString("univ_loc"));
		profile.setUnivName(resultSet.getString("univ_name"));
		profile.setUnivFac(resultSet.getString("univ_fac"));
		profile.setUnivDep(resultSet.getString("univ_dep"));
		profile.setGradLoc(resultSet.getString("grad_loc"));
		profile.setGradName(resultSet.getString("grad_name"));
		profile.setGradSchool(resultSet.getString("grad_school"));
		profile.setGradDiv(resultSet.getString("grad_div"));
		profile.setGraduatedIn(resultSet.getString("graduated_in"));
		profile.setDegree(resultSet.getString("degree"));
		profile.setClub(resultSet.getString("club"));
		profile.setLikes(new ArrayList<String>(Arrays.asList(resultSet.getString("likes").split(","))));
		profile.setPosition(new ArrayList<String>(Arrays.asList(resultSet.getString("position").split(","))));
		return profile;
	}

	private Student adjustToStu(Student student, ResultSet resultSet) throws SQLException {
		student.setEmail(resultSet.getString("email"));
		student.setValidEmail(resultSet.getBoolean("valid_email"));
		Date lastLogin = resultSet.getTimestamp("last_login");
		student.setLastLogin(LocalDateTime.ofInstant(lastLogin.toInstant(), ZoneId.of("Asia/Tokyo")));
		if (!(Objects.equals(resultSet.getString("family_name"), null))) {
			student.setFamilyName(resultSet.getString("family_name"));
			student.setGivenName(resultSet.getString("given_name"));
			student.setKanaFamilyName(resultSet.getString("kana_family_name"));
			student.setKanaGivenName(resultSet.getString("kana_given_name"));
			Date birth = resultSet.getTimestamp("date_of_birth");
			student.setBirth(LocalDate.ofInstant(birth.toInstant(), ZoneId.systemDefault()));
			student.setSex(resultSet.getString("sex"));
			student.setPhone(resultSet.getString("phone"));
			student.setMajor(resultSet.getString("major"));
			student.setUnivLoc(resultSet.getString("univ_loc"));
			student.setUnivName(resultSet.getString("univ_name"));
			student.setUnivFac(resultSet.getString("univ_fac"));
			student.setUnivDep(resultSet.getString("univ_dep"));
			student.setGradLoc(resultSet.getString("grad_loc"));
			student.setGradName(resultSet.getString("grad_name"));
			student.setGradSchool(resultSet.getString("grad_school"));
			student.setGradDiv(resultSet.getString("grad_div"));
			student.setGraduatedIn(resultSet.getString("graduated_in"));
			student.setDegree(resultSet.getString("degree"));
			student.setClub(resultSet.getString("club"));
			student.setLikes(new ArrayList<String>(Arrays.asList(resultSet.getString("likes").split(","))));
			student.setPosition(new ArrayList<String>(Arrays.asList(resultSet.getString("position").split(","))));
			student.convertForDisplay();
		}
		return student;
	}

	private Map<String, Object> findStudents(List<String> target, List<String> order, Map<String, Object> params) {
		List<String> elements = new LinkedList<String>(Arrays.asList("SELECT", "COUNT(email)", "FROM profiles"));
		if (!Objects.equals(target, null) && !target.isEmpty()) elements.addAll(Arrays.asList("WHERE", target.stream().collect(Collectors.joining(" AND "))));
		String sql = elements.stream().collect(Collectors.joining(" "));
		Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
		elements.set(1, this.COLUMNS.stream().collect(Collectors.joining(", ")));
		elements.set(2, "FROM profiles INNER JOIN accounts ON profiles.email = accounts.email");
		if (!Objects.equals(order, null) && !order.isEmpty()) elements.addAll(Arrays.asList("ORDER BY", order.stream().collect(Collectors.joining(", "))));
		sql = elements.stream().collect(Collectors.joining(" "));
		List<Student> students = jdbcTemplate.query(sql, params, (resultSet, i) -> {
			Student student = new Student();
			return adjustToStu(student, resultSet);
		});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("students", students);
		return result;
	}
}
