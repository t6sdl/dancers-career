package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.repository.ProfileRepository;

@Repository
public class JdbcProfileRepository implements ProfileRepository {
	private JdbcTemplate jdbcTemplate;
	
	public JdbcProfileRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private List<String> stringToList(String str) {
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(str.split(","));
		return list;
	}
	
	private String listToString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			builder.append(list.get(i));
			if (i < list.size() - 1) {
				builder.append(",");
			}
		}
		String str = builder.toString();
		return str;
	}

	@Override
	public List<Profile> find() {
		return jdbcTemplate.query(
				"SELECT * FROM profiles ORDER BY email", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				});
	}
	
	@Override
	public Profile findOneByEmail(String email) {
		 try {
				return jdbcTemplate.queryForObject(
						"SELECT * FROM profiles WHERE email = ?", (resultSet, i) -> {
							Profile profile = new Profile();
							this.adjustDataToProfile(profile, resultSet);
							return profile;
						}, email);
		 } catch (EmptyResultDataAccessException e) {
			 return null;
		 }
	}
	
	@Override
	public List<Profile> findByName(String kanaLastName, String kanaFirstName) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE kana_last_name = ? AND kana_first_name = ? ORDER BY email ASC", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				}, kanaLastName, kanaFirstName);
	}

	@Override
	public List<Profile> findByLastName(String kanaLastName) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE kana_last_name = ? ORDER BY email ASC", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				}, kanaLastName);
	}

	@Override
	public List<Profile> findByPrefecture(String prefecture) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE prefecture = ? ORDER BY email ASC", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				}, prefecture);
	}

	@Override
	public List<Profile> findByUniversity(String prefecture, String university) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE prefecture = ? AND university = ? ORDER BY email ASC", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				}, prefecture, university);
	}

	@Override
	public List<Profile> findByFaculty(String prefecture, String university, String faculty) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE prefecture = ? AND university = ? AND faculty = ? ORDER BY email ASC", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				}, prefecture, university, faculty);
	}

	@Override
	public List<Profile> findByDepartment(String prefecture, String university, String faculty, String department) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE prefecture = ? AND university = ? AND faculty = ? AND department = ? ORDER BY email ASC", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				}, prefecture, university, faculty, department);
	}

	@Override
	public List<Profile> findByPosition(List<String> position, String method) {
		StringBuffer like = new StringBuffer();
		for (int i = 0; i < position.size(); i++) {
			like.append("position LIKE '%").append(position.get(i)).append("%'");
			if (i < position.size() - 1) {
				like.append(" " + method + " ");
			}
		}
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE " + like + " ORDER BY email ASC", (resultSet, i) -> {
					Profile profile = new Profile();
					this.adjustDataToProfile(profile, resultSet);
					return profile;
				});
	}
	
	@Override
	public String findLastNameByEmail(String email) {
		try {
			return jdbcTemplate.queryForObject("SELECT (last_name) FROM profiles WHERE email = ?", String.class, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public List<String> findLikesByEmail(String email) {
		try {
			String likesData = jdbcTemplate.queryForObject("SELECT (likes) FROM profiles WHERE email = ?", String.class, email);
			return this.stringToList(likesData);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<String>(Arrays.asList(""));
		}
	}

	@Override
	public void insert(Profile newProfile) {
		String position = listToString(newProfile.getPosition());
		Date date_of_birth = Date.from(newProfile.getDate_of_birth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		jdbcTemplate.update(
				"INSERT INTO profiles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				newProfile.getEmail(), newProfile.getLast_name(), newProfile.getFirst_name(), newProfile.getKana_last_name(), newProfile.getKana_first_name(),
				date_of_birth, newProfile.getSex(), newProfile.getPhone_number(), newProfile.getMajor(), newProfile.getPrefecture(), newProfile.getUniversity(),
				newProfile.getFaculty(), newProfile.getDepartment(), newProfile.getGraduation(), newProfile.getAcademic_degree(), position, "");
	}

	@Override
	public void delete(String email) {
		jdbcTemplate.update(
				"DELETE FROM profiles WHERE email = ?",
				email);
	}

	@Override
	public void updateAny(Profile profile) {
		String position = listToString(profile.getPosition());
		Date date_of_birth = Date.from(profile.getDate_of_birth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		jdbcTemplate.update(
				"UPDATE profiles SET last_name = ?, first_name = ?, kana_last_name = ?, kana_first_name = ?, date_of_birth = ?, sex = ?, phone_number = ?, "
				+ "major = ?, prefecture = ?, university = ?, faculty = ?, department = ?, graduation = ?, academic_degree = ?, position = ? WHERE email = ?",
				profile.getLast_name(), profile.getFirst_name(), profile.getKana_last_name(), profile.getKana_first_name(),
				date_of_birth, profile.getSex(), profile.getPhone_number(), profile.getMajor(), profile.getPrefecture(), profile.getUniversity(),
				profile.getFaculty(), profile.getDepartment(), profile.getGraduation(), profile.getAcademic_degree(), position, profile.getEmail());
	}
	
	@Override
	public void updateLikes(String email, List<String> likes) {
		String likesData;
		if (likes.isEmpty()) {
			likesData = "";
		} else {
			likesData = this.listToString(likes);
		}
		jdbcTemplate.update("UPDATE profiles SET likes = ? WHERE email = ?", likesData, email);
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
		profile.setPrefecture(resultSet.getString("prefecture"));
		profile.setUniversity(resultSet.getString("university"));
		profile.setFaculty(resultSet.getString("faculty"));
		profile.setDepartment(resultSet.getString("department"));
		profile.setGraduation(resultSet.getString("graduation"));
		profile.setAcademic_degree(resultSet.getString("academic_degree"));
		profile.setPosition(this.stringToList(resultSet.getString("position")));
		profile.setLikes(this.stringToList(resultSet.getString("likes")));
	}
}
