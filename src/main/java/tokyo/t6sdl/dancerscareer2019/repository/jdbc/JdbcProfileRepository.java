package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.repository.ProfileRepository;

@RequiredArgsConstructor
@Repository
public class JdbcProfileRepository implements ProfileRepository {
	private final JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(JdbcProfileRepository.class);
	
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
		if (position.contains("")) {
			return null;
		} else if (position.size() == 1) {
			method = "OR";
		}
		List<List<String>> results = new ArrayList<List<String>>();
		position.forEach(pos -> {
			List<String> result = jdbcTemplate.query(
					"SELECT (email) FROM positions WHERE position = ?", (resultSet, i) -> {
						return resultSet.getString("email");
					}, pos);
			if (!(Objects.equals(result, null))) {
				results.add(result);
			}
		});
		logger.info("results[0]: " + results.get(0).toString() + " in findByPosition()");
		Set<String> emails = new HashSet<String>();
		switch (method) {
		case "OR":
			results.forEach(result -> {
				emails.addAll(result);
			});
			break;
		case "AND":
			results.get(0).forEach(email -> {
				boolean isRepeated = true;
				for (int i = 1; i < results.size(); i++) {
					if (!(results.get(i).contains(email))) {
						isRepeated = false;
						break;
					}
				}
				if (isRepeated) {
					emails.add(email);
				}
			});
			break;
		default:
			return null;
		}
		logger.info("emails: " + emails.toString() + " in findByPosition()");
		List<Profile> profiles = new ArrayList<Profile>();
		emails.forEach(email -> {
			profiles.add(this.findOneByEmail(email));
		});
		return profiles;
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
		Date date_of_birth = Date.from(newProfile.getDate_of_birth().atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant());
		jdbcTemplate.update(
				"INSERT INTO profiles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				newProfile.getEmail(), newProfile.getLast_name(), newProfile.getFirst_name(), newProfile.getKana_last_name(), newProfile.getKana_first_name(),
				date_of_birth, newProfile.getSex(), newProfile.getPhone_number(), newProfile.getMajor(), newProfile.getPrefecture(), newProfile.getUniversity(),
				newProfile.getFaculty(), newProfile.getDepartment(), newProfile.getGraduation(), newProfile.getAcademic_degree(), "");
		if (!(newProfile.getPosition().contains(""))) {
			newProfile.getPosition().forEach(position -> {
				jdbcTemplate.update("INSERT INTO positions VALUES (?, ?)", newProfile.getEmail(), position);
			});
		}
	}

	@Override
	public void delete(String email) {
		jdbcTemplate.update("DELETE FROM positions WHERE email = ?", email);
		jdbcTemplate.update("DELETE FROM profiles WHERE email = ?", email);
	}

	@Override
	public void updateAny(Profile profile) {
		Date date_of_birth = Date.from(profile.getDate_of_birth().atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant());
		jdbcTemplate.update(
				"UPDATE profiles SET last_name = ?, first_name = ?, kana_last_name = ?, kana_first_name = ?, date_of_birth = ?, sex = ?, phone_number = ?, "
				+ "major = ?, prefecture = ?, university = ?, faculty = ?, department = ?, graduation = ?, academic_degree = ? WHERE email = ?",
				profile.getLast_name(), profile.getFirst_name(), profile.getKana_last_name(), profile.getKana_first_name(),
				date_of_birth, profile.getSex(), profile.getPhone_number(), profile.getMajor(), profile.getPrefecture(), profile.getUniversity(),
				profile.getFaculty(), profile.getDepartment(), profile.getGraduation(), profile.getAcademic_degree(), profile.getEmail());
		jdbcTemplate.update("DELETE FROM positions WHERE email = ?", profile.getEmail());
		if (!(profile.getPosition().contains(""))) {
			profile.getPosition().forEach(position -> {
				jdbcTemplate.update("INSERT INTO positions VALUES (?, ?)", profile.getEmail(), position);
			});
		}
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
		profile.setDate_of_birth(LocalDate.ofInstant(dateOfBirth.toInstant(), ZoneId.of("Asia/Tokyo")));
		profile.setSex(resultSet.getString("sex"));
		profile.setPhone_number(resultSet.getString("phone_number"));
		profile.setMajor(resultSet.getString("major"));
		profile.setPrefecture(resultSet.getString("prefecture"));
		profile.setUniversity(resultSet.getString("university"));
		profile.setFaculty(resultSet.getString("faculty"));
		profile.setDepartment(resultSet.getString("department"));
		profile.setGraduation(resultSet.getString("graduation"));
		profile.setAcademic_degree(resultSet.getString("academic_degree"));
		profile.setLikes(this.stringToList(resultSet.getString("likes")));
		List<String> position = jdbcTemplate.query("SELECT (position) FROM positions WHERE email = ?", (posSet, i) -> {
			return posSet.getString("position");
		}, profile.getEmail());
		profile.setPosition(position);
		logger.info("position: " + position.toString() + " in adjustDataToProfile()");
	}
}
