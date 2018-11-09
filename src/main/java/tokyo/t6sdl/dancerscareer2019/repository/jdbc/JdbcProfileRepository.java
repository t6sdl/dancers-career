package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.model.Student;
import tokyo.t6sdl.dancerscareer2019.repository.ProfileRepository;

@RequiredArgsConstructor
@Repository
public class JdbcProfileRepository implements ProfileRepository {
	private final JdbcTemplate jdbcTemplate;
	private final String QUERIED_VALUE = "accounts.email, valid_email, last_login, last_name, first_name, kana_last_name, kana_first_name, date_of_birth, sex, phone_number, major, univ_pref, univ_name, faculty, department, grad_school_pref, grad_school_name, grad_school_of, program_in, graduation, academic_degree, position, likes";
	private final List<String> SORT_LIST = Arrays.asList("last_login DESC", "kana_last_name ASC, kana_first_name ASC", "univ_pref ASC, univ_name ASC, faculty ASC, department ASC");
	
	private List<String> stringToList(String str) {
		return new ArrayList<String>(Arrays.asList(str.split(",")));
	}
	
	private String listToString(List<String> list) {
		return list.toString().substring(1, list.toString().length() - 1).replace(" ", "");
	}
	
	private String listToString(List<String> list, String prefix, String suffix, String separator) {
		StringJoiner joiner = new StringJoiner(separator, prefix, suffix);
		list.forEach(str -> joiner.add(str));
		return joiner.toString();
	}
	
	@Override
	public Profile findOneByEmail(String email) {
		 try {
				return jdbcTemplate.queryForObject(
						"SELECT email, last_name, first_name, kana_last_name, kana_first_name, date_of_birth, sex, phone_number, major, univ_pref, univ_name, faculty, department, grad_school_pref, grad_school_name, grad_school_of, program_in, graduation, academic_degree, position, likes FROM profiles WHERE email = ?", (resultSet, i) -> {
							Profile profile = new Profile();
							this.adjustDataToProfile(profile, resultSet);
							return profile;
						}, email);
		 } catch (EmptyResultDataAccessException e) {
			 return null;
		 }
	}

	@Override
	public Map<String, Object> find(int sort) {
		Integer count = jdbcTemplate.queryForObject("SELECT count FROM counts WHERE name = 'profiles'", Integer.class);
		List<String> emails = new ArrayList<String>();
		List<Student> students = jdbcTemplate.query(
				this.selectStudentIn("WHERE authority = 'ROLE_USER'", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					emails.add(student.getEmail());
					return student;
				});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("emails", emails);
		result.put("students", students);
		return result;
	}
	
	@Override
	public Map<String, Object> findByName(int sort, String kanaLastName, String kanaFirstName) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM profiles WHERE kana_last_name = ? AND kana_first_name = ?", Integer.class, kanaLastName, kanaFirstName);
		List<String> emails = new ArrayList<String>();
		List<Student> students = jdbcTemplate.query(
				this.selectStudentIn("WHERE kana_last_name = ? AND kana_first_name = ?", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					emails.add(student.getEmail());
					return student;
				}, kanaLastName, kanaFirstName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("emails", emails);
		result.put("students", students);
		return result;
	}

	@Override
	public Map<String, Object> findByLastName(int sort, String kanaLastName) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM profiles WHERE kana_last_name = ?", Integer.class, kanaLastName);
		List<String> emails = new ArrayList<String>();
		List<Student> students = jdbcTemplate.query(
				this.selectStudentIn("WHERE kana_last_name = ?", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					emails.add(student.getEmail());
					return student;
				}, kanaLastName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("emails", emails);
		result.put("students", students);
		return result;
	}

	@Override
	public Map<String, Object> findByPrefecture(int sort, String univPref) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM profiles WHERE univ_pref = ?", Integer.class, univPref);
		List<String> emails = new ArrayList<String>();
		List<Student> students = jdbcTemplate.query(
				this.selectStudentIn("WHERE univ_pref = ?", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					emails.add(student.getEmail());
					return student;
				}, univPref);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("emails", emails);
		result.put("students", students);
		return result;
	}

	@Override
	public Map<String, Object> findByUniversity(int sort, String univPref, String univName) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM profiles WHERE univ_pref = ? AND univ_name = ?", Integer.class, univPref, univName);
		List<String> emails = new ArrayList<String>();
		List<Student> students = jdbcTemplate.query(
				this.selectStudentIn("WHERE univ_pref = ? AND univ_name = ?", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					emails.add(student.getEmail());
					return student;
				}, univPref, univName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("emails", emails);
		result.put("students", students);
		return result;
	}

	@Override
	public Map<String, Object> findByFaculty(int sort, String univPref, String univName, String faculty) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM profiles WHERE univ_pref = ? AND univ_name = ? AND faculty = ?", Integer.class, univPref, univName, faculty);
		List<String> emails = new ArrayList<String>();
		List<Student> students = jdbcTemplate.query(
				this.selectStudentIn("WHERE univ_pref = ? AND univ_name = ? AND faculty = ?", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					emails.add(student.getEmail());
					return student;
				}, univPref, univName, faculty);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("emails", emails);
		result.put("students", students);
		return result;
	}

	@Override
	public Map<String, Object> findByDepartment(int sort, String univPref, String univName, String faculty, String department) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM profiles WHERE univ_pref = ? AND univ_name = ? AND faculty = ? AND department = ?", Integer.class, univPref, univName, faculty, department);
		List<String> emails = new ArrayList<String>();
		List<Student> students = jdbcTemplate.query(
				this.selectStudentIn("WHERE univ_pref = ? AND univ_name = ? AND faculty = ? AND department = ?", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					emails.add(student.getEmail());
					return student;
				}, univPref, univName, faculty, department);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("emails", emails);
		result.put("students", students);
		return result;
	}

	@Override
	public Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", 0);
		result.put("emails", null);
		result.put("students", null);
		if (position.contains("")) return result;
		String posStr = this.listToString(position, "'", "'", ", ");
		List<String> emails = new ArrayList<String>();
		if (andSearch) {
			emails.addAll(jdbcTemplate.query(
					"SELECT email FROM positions WHERE position IN (" + posStr + ") GROUP BY email HAVING COUNT(email) = ? ORDER BY email DESC", (resultSet, i) -> {
						return resultSet.getString("email");
					}, position.size()));
		} else {
			emails.addAll(jdbcTemplate.query(
					"SELECT email FROM positions WHERE position IN (" + posStr + ") GROUP BY email ORDER BY email DESC", (resultSet, i) -> {
						return resultSet.getString("email");
					}));
		}
		if (Objects.equals(emails, null) || emails.isEmpty()) return result;
		List<Student> students = this.findByEmail(sort, emails);
		result.replace("count", emails.size());
		result.replace("emails", emails);
		result.replace("students", students);
		return result;
	}
	
	@Override
	public String findLastNameByEmail(String email) {
		try {
			return jdbcTemplate.queryForObject("SELECT last_name FROM profiles WHERE email = ?", String.class, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public List<String> findLikesByEmail(String email) {
		try {
			String likes = jdbcTemplate.queryForObject("SELECT likes FROM profiles WHERE email = ?", String.class, email);
			return this.stringToList(likes);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<String>(Arrays.asList(""));
		}
	}

	@Override
	public void insert(Profile newProfile) {
		Date date_of_birth = Date.from(newProfile.getDate_of_birth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		jdbcTemplate.update(
				"INSERT INTO profiles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				newProfile.getEmail(), newProfile.getLast_name(), newProfile.getFirst_name(), newProfile.getKana_last_name(), newProfile.getKana_first_name(),
				date_of_birth, newProfile.getSex(), newProfile.getPhone_number(), newProfile.getMajor(), newProfile.getUniv_pref(), newProfile.getUniv_name(),
				newProfile.getFaculty(), newProfile.getDepartment(), newProfile.getGrad_school_pref(), newProfile.getGrad_school_name(), newProfile.getGrad_school_of(),
				newProfile.getProgram_in(), newProfile.getGraduation(), newProfile.getAcademic_degree(), this.listToString(newProfile.getPosition()), "");
		if (!(newProfile.getPosition().contains(""))) {
			String insertPos = this.listToString(newProfile.getPosition(), "('", "', '" + newProfile.getEmail() + "')", ", ");
			jdbcTemplate.update("INSERT INTO positions VALUES " + insertPos);
		}
		jdbcTemplate.update("UPDATE counts SET count = count + 1 WHERE name = 'profiles'");
	}

	@Override
	public void delete(String email) {
		jdbcTemplate.update("DELETE FROM positions WHERE email = ?", email);
		jdbcTemplate.update("DELETE FROM profiles WHERE email = ?", email);
		jdbcTemplate.update("UPDATE counts SET count = CASE WHEN count = 0 THEN 0 ELSE count - 1 END WHERE name = 'profiles'");
	}

	@Override
	public void updateAny(Profile profile) {
		List<String> oldPos = this.stringToList(jdbcTemplate.queryForObject("SELECT position FROM profiles WHERE email = ?", String.class, profile.getEmail()));
		List<String> newPos = profile.getPosition();
		Date date_of_birth = Date.from(profile.getDate_of_birth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		jdbcTemplate.update(
				"UPDATE profiles SET last_name = ?, first_name = ?, kana_last_name = ?, kana_first_name = ?, date_of_birth = ?, sex = ?, phone_number = ?, major = ?, univ_pref = ?, univ_name = ?, "
				+ "faculty = ?, department = ?, grad_school_pref = ?, grad_school_name = ?, grad_school_of = ?, program_in = ?, graduation = ?, academic_degree = ?, position = ? WHERE email = ?",
				profile.getLast_name(), profile.getFirst_name(), profile.getKana_last_name(), profile.getKana_first_name(), date_of_birth, profile.getSex(), profile.getPhone_number(),
				profile.getMajor(), profile.getUniv_pref(), profile.getUniv_name(), profile.getFaculty(), profile.getDepartment(), profile.getGrad_school_pref(), profile.getGrad_school_name(),
				profile.getGrad_school_of(), profile.getProgram_in(), profile.getGraduation(), profile.getAcademic_degree(), this.listToString(newPos), profile.getEmail());
		List<String> duplication = new ArrayList<String>();
		oldPos.forEach(pos -> {
			if (newPos.contains(pos)) duplication.add(pos);
		});
		oldPos.removeAll(duplication);
		newPos.removeAll(duplication);
		if (!(oldPos.isEmpty())) {
			String deletePos = this.listToString(oldPos, "'", "'", ", ");
			jdbcTemplate.update("DELETE FROM positions WHERE position IN (" + deletePos + ") AND email = ?", profile.getEmail());
		}
		if (!(newPos.isEmpty())) {
			String insertPos = this.listToString(newPos, "('", "', '" + profile.getEmail() + "')", ", ");
			jdbcTemplate.update("INSERT INTO positions VALUES " + insertPos);
		}
	}
	
	@Override
	public void updateLikes(String email, List<String> likes) {
		jdbcTemplate.update("UPDATE profiles SET likes = ? WHERE email = ?", this.listToString(likes), email);
	}
	
	protected List<Student> findByEmail(int sort, List<String> emails) {
		if (Objects.equals(emails, null) || emails.isEmpty()) {
			return null;
		}
		 String emailsStr = this.listToString(emails, "'", "'", ", ");
		return jdbcTemplate.query(
				this.selectStudentIn("WHERE accounts.email IN (" + emailsStr + ")", sort), (resultSet, i) -> {
					Student student = new Student();
					this.adjustDataToStudent(student, resultSet);
					return student;
				});
	}
	
	private void adjustDataToProfile(Profile profile, ResultSet resultSet) throws SQLException {
		profile.setEmail(resultSet.getString("email"));
		profile.setLast_name(resultSet.getString("last_name"));
		profile.setFirst_name(resultSet.getString("first_name"));
		profile.setKana_last_name(resultSet.getString("kana_last_name"));
		profile.setKana_first_name(resultSet.getString("kana_first_name"));
		Date dateOfBirth = resultSet.getTimestamp("date_of_birth");
		profile.setDate_of_birth(LocalDate.ofInstant(dateOfBirth.toInstant(), ZoneId.systemDefault()));
		profile.setSex(resultSet.getString("sex"));
		profile.setPhone_number(resultSet.getString("phone_number"));
		profile.setMajor(resultSet.getString("major"));
		profile.setUniv_pref(resultSet.getString("univ_pref"));
		profile.setUniv_name(resultSet.getString("univ_name"));
		profile.setFaculty(resultSet.getString("faculty"));
		profile.setDepartment(resultSet.getString("department"));
		profile.setGrad_school_pref(resultSet.getString("grad_school_pref"));
		profile.setGrad_school_name(resultSet.getString("grad_school_name"));
		profile.setGrad_school_of(resultSet.getString("grad_school_of"));
		profile.setProgram_in(resultSet.getString("program_in"));
		profile.setGraduation(resultSet.getString("graduation"));
		profile.setAcademic_degree(resultSet.getString("academic_degree"));
		profile.setLikes(this.stringToList(resultSet.getString("likes")));
		profile.setPosition(this.stringToList(resultSet.getString("position")));
	}
	
	private void adjustDataToStudent(Student student, ResultSet resultSet) throws SQLException {
		student.setEmail(resultSet.getString("email"));
		student.setValid_email(resultSet.getBoolean("valid_email"));
		Date lastLogin = resultSet.getTimestamp("last_login");
		student.setLast_login(LocalDateTime.ofInstant(lastLogin.toInstant(), ZoneId.of("Asia/Tokyo")));
		if (!(Objects.equals(resultSet.getString("last_name"), null))) {
			student.setLast_name(resultSet.getString("last_name"));
			student.setFirst_name(resultSet.getString("first_name"));
			student.setKana_last_name(resultSet.getString("kana_last_name"));
			student.setKana_first_name(resultSet.getString("kana_first_name"));
			Date dateOfBirth = resultSet.getTimestamp("date_of_birth");
			student.setDate_of_birth(LocalDate.ofInstant(dateOfBirth.toInstant(), ZoneId.systemDefault()));
			student.setSex(resultSet.getString("sex"));
			student.setPhone_number(resultSet.getString("phone_number"));
			student.setMajor(resultSet.getString("major"));
			student.setUniv_pref(resultSet.getString("univ_pref"));
			student.setUniv_name(resultSet.getString("univ_name"));
			student.setFaculty(resultSet.getString("faculty"));
			student.setDepartment(resultSet.getString("department"));
			student.setGrad_school_pref(resultSet.getString("grad_school_pref"));
			student.setGrad_school_name(resultSet.getString("grad_school_name"));
			student.setGrad_school_of(resultSet.getString("grad_school_of"));
			student.setProgram_in(resultSet.getString("program_in"));
			student.setGraduation(resultSet.getString("graduation"));
			student.setAcademic_degree(resultSet.getString("academic_degree"));
			student.setLikes(this.stringToList(resultSet.getString("likes")));
			student.setPosition(this.stringToList(resultSet.getString("position")));
			student.convertForDisplay();
		}
	}
	
	private String selectStudentIn(String condition, int sort) {
		if (sort > 2) sort = 0;
		return "SELECT " + this.QUERIED_VALUE + " FROM accounts LEFT OUTER JOIN profiles ON accounts.email = profiles.email " + condition + " GROUP BY " + this.QUERIED_VALUE + " ORDER BY " + this.SORT_LIST.get(sort);
	}
}
