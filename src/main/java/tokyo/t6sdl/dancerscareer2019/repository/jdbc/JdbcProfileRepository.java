package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	public List<String> stringToList(String str) {
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(str.split(","));
		return list;
	}
	
	public String listToString(List<String> list) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			buf.append(list.get(i));
			if (i < list.size() - 1) {
				buf.append(",");
			}
		}
		String str = buf.toString();
		return str;
	}

	@Override
	public List<Profile> find() {
		return jdbcTemplate.query(
				"SELECT * FROM profiles ORDER BY email", (resultSet, i) -> {
					Profile profile = new Profile();
					profile.setEmail(resultSet.getString("email"));
					profile.setLast_name(resultSet.getString("last_name"));
					profile.setFirst_name(resultSet.getString("first_name"));
					profile.setKana_last_name(resultSet.getString("kana_last_name"));
					profile.setKana_first_name(resultSet.getString("kana_first_name"));
					profile.setDate_of_birth(resultSet.getTimestamp("date_of_birth"));
					profile.setSex(resultSet.getString("sex"));
					profile.setPhone_number(resultSet.getString("phone_number"));
					profile.setMajor(resultSet.getString("major"));
					profile.setUniversity(resultSet.getString("university"));
					profile.setFaculty(resultSet.getString("faculty"));
					profile.setDepartment(resultSet.getString("department"));
					profile.setGraduation(resultSet.getString("graduation"));
					profile.setAcademic_degree(resultSet.getString("academic_degree"));
					profile.setPosition(stringToList(resultSet.getString("position")));
					return profile;
				});
	}
	
	@Override
	public Profile findOneByEmail(String email) {
		 try {
				return jdbcTemplate.queryForObject(
						"SELECT * FROM profiles WHERE email = ?", (resultSet, i) -> {
							Profile profile = new Profile();
							profile.setEmail(resultSet.getString("email"));
							profile.setLast_name(resultSet.getString("last_name"));
							profile.setFirst_name(resultSet.getString("first_name"));
							profile.setKana_last_name(resultSet.getString("kana_last_name"));
							profile.setKana_first_name(resultSet.getString("kana_first_name"));
							profile.setDate_of_birth(resultSet.getTimestamp("date_of_birth"));
							profile.setSex(resultSet.getString("sex"));
							profile.setPhone_number(resultSet.getString("phone_number"));
							profile.setMajor(resultSet.getString("major"));
							profile.setUniversity(resultSet.getString("university"));
							profile.setFaculty(resultSet.getString("faculty"));
							profile.setDepartment(resultSet.getString("department"));
							profile.setGraduation(resultSet.getString("graduation"));
							profile.setAcademic_degree(resultSet.getString("academic_degree"));
							profile.setPosition(stringToList(resultSet.getString("position")));
							return profile;
						}, email);
		 } catch (EmptyResultDataAccessException e) {
			 return null;
		 }
	}

	@Override
	public List<Profile> findByUniversity(String university) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE university = ? ORDER BY email", (resultSet, i) -> {
					Profile profile = new Profile();
					profile.setEmail(resultSet.getString("email"));
					profile.setLast_name(resultSet.getString("last_name"));
					profile.setFirst_name(resultSet.getString("first_name"));
					profile.setKana_last_name(resultSet.getString("kana_last_name"));
					profile.setKana_first_name(resultSet.getString("kana_first_name"));
					profile.setDate_of_birth(resultSet.getTimestamp("date_of_birth"));
					profile.setSex(resultSet.getString("sex"));
					profile.setPhone_number(resultSet.getString("phone_number"));
					profile.setMajor(resultSet.getString("major"));
					profile.setUniversity(resultSet.getString("university"));
					profile.setFaculty(resultSet.getString("faculty"));
					profile.setDepartment(resultSet.getString("department"));
					profile.setGraduation(resultSet.getString("graduation"));
					profile.setAcademic_degree(resultSet.getString("academic_degree"));
					profile.setPosition(stringToList(resultSet.getString("position")));
					return profile;
				}, university);
	}

	@Override
	public List<Profile> findByFaculty(String university, String faculty) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE university = ? AND faculty = ? ORDER BY email", (resultSet, i) -> {
					Profile profile = new Profile();
					profile.setEmail(resultSet.getString("email"));
					profile.setLast_name(resultSet.getString("last_name"));
					profile.setFirst_name(resultSet.getString("first_name"));
					profile.setKana_last_name(resultSet.getString("kana_last_name"));
					profile.setKana_first_name(resultSet.getString("kana_first_name"));
					profile.setDate_of_birth(resultSet.getTimestamp("date_of_birth"));
					profile.setSex(resultSet.getString("sex"));
					profile.setPhone_number(resultSet.getString("phone_number"));
					profile.setMajor(resultSet.getString("major"));
					profile.setUniversity(resultSet.getString("university"));
					profile.setFaculty(resultSet.getString("faculty"));
					profile.setDepartment(resultSet.getString("department"));
					profile.setGraduation(resultSet.getString("graduation"));
					profile.setAcademic_degree(resultSet.getString("academic_degree"));
					profile.setPosition(stringToList(resultSet.getString("position")));
					return profile;
				}, university, faculty);
	}

	@Override
	public List<Profile> findByDepartment(String university, String faculty, String department) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE university = ? AND faculty = ? AND department = ? ORDER BY email", (resultSet, i) -> {
					Profile profile = new Profile();
					profile.setEmail(resultSet.getString("email"));
					profile.setLast_name(resultSet.getString("last_name"));
					profile.setFirst_name(resultSet.getString("first_name"));
					profile.setKana_last_name(resultSet.getString("kana_last_name"));
					profile.setKana_first_name(resultSet.getString("kana_first_name"));
					profile.setDate_of_birth(resultSet.getTimestamp("date_of_birth"));
					profile.setSex(resultSet.getString("sex"));
					profile.setPhone_number(resultSet.getString("phone_number"));
					profile.setMajor(resultSet.getString("major"));
					profile.setUniversity(resultSet.getString("university"));
					profile.setFaculty(resultSet.getString("faculty"));
					profile.setDepartment(resultSet.getString("department"));
					profile.setGraduation(resultSet.getString("graduation"));
					profile.setAcademic_degree(resultSet.getString("academic_degree"));
					profile.setPosition(stringToList(resultSet.getString("position")));
					return profile;
				}, university, faculty, department);
	}

	@Override
	public List<Profile> findByFacultyOnly(String faculty) {
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE faculty = ? ORDER BY email", (resultSet, i) -> {
					Profile profile = new Profile();
					profile.setEmail(resultSet.getString("email"));
					profile.setLast_name(resultSet.getString("last_name"));
					profile.setFirst_name(resultSet.getString("first_name"));
					profile.setKana_last_name(resultSet.getString("kana_last_name"));
					profile.setKana_first_name(resultSet.getString("kana_first_name"));
					profile.setDate_of_birth(resultSet.getTimestamp("date_of_birth"));
					profile.setSex(resultSet.getString("sex"));
					profile.setPhone_number(resultSet.getString("phone_number"));
					profile.setMajor(resultSet.getString("major"));
					profile.setUniversity(resultSet.getString("university"));
					profile.setFaculty(resultSet.getString("faculty"));
					profile.setDepartment(resultSet.getString("department"));
					profile.setGraduation(resultSet.getString("graduation"));
					profile.setAcademic_degree(resultSet.getString("academic_degree"));
					profile.setPosition(stringToList(resultSet.getString("position")));
					return profile;
				}, faculty);
	}
	
	@Override
	public List<Profile> findByPosition(List<String> position) {
		StringBuffer like = new StringBuffer();
		for (int i = 0; i < position.size(); i++) {
			like.append("position LIKE '%").append(position.get(i)).append("%'");
			if (i < position.size() - 1) {
				like.append(" AND ");
			}
		}
		return jdbcTemplate.query(
				"SELECT * FROM profiles WHERE " + like + " ORDER BY email", (resultSet, i) -> {
					Profile profile = new Profile();
					profile.setEmail(resultSet.getString("email"));
					profile.setLast_name(resultSet.getString("last_name"));
					profile.setFirst_name(resultSet.getString("first_name"));
					profile.setKana_last_name(resultSet.getString("kana_last_name"));
					profile.setKana_first_name(resultSet.getString("kana_first_name"));
					profile.setDate_of_birth(resultSet.getTimestamp("date_of_birth"));
					profile.setSex(resultSet.getString("sex"));
					profile.setPhone_number(resultSet.getString("phone_number"));
					profile.setMajor(resultSet.getString("major"));
					profile.setUniversity(resultSet.getString("university"));
					profile.setFaculty(resultSet.getString("faculty"));
					profile.setDepartment(resultSet.getString("department"));
					profile.setGraduation(resultSet.getString("graduation"));
					profile.setAcademic_degree(resultSet.getString("academic_degree"));
					profile.setPosition(stringToList(resultSet.getString("position")));
					return profile;
				});
	}

	@Override
	public void insert(Profile newProfile) {
		String positions = listToString(newProfile.getPosition());
		System.out.println(newProfile.getEmail() + ", " + newProfile.getLast_name() + ", " + newProfile.getFirst_name() + ", " + newProfile.getKana_last_name() + ", " + newProfile.getKana_first_name() + ", " + newProfile.getDate_of_birth() + ", " + newProfile.getSex() + ", " + newProfile.getPhone_number() + ", " + newProfile.getMajor() + ", " + newProfile.getUniversity() + ", " + newProfile.getFaculty() + ", " + newProfile.getDepartment() + ", " + newProfile.getGraduation() + ", " + newProfile.getAcademic_degree() + ", " + positions);
		jdbcTemplate.update(
				"INSERT INTO profiles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				newProfile.getEmail(), newProfile.getLast_name(), newProfile.getFirst_name(), newProfile.getKana_last_name(), newProfile.getKana_first_name(),
				newProfile.getDate_of_birth(), newProfile.getSex(), newProfile.getPhone_number(), newProfile.getMajor(), newProfile.getUniversity(),
				newProfile.getFaculty(), newProfile.getDepartment(), newProfile.getGraduation(), newProfile.getAcademic_degree(), positions);
	}

	@Override
	public void delete(String email) {
		jdbcTemplate.update(
				"DELETE FROM profiles WHERE email = ?",
				email);
	}

	@Override
	public void updateAny(Profile profile) {
		String positions = listToString(profile.getPosition());
		jdbcTemplate.update(
				"UPDATE profiles SET last_name = ?, first_name = ?, kana_last_name = ?, kana_first_name = ?, date_of_birth = ?, sex = ?, phone_number = ?, "
				+ "major = ?, university = ?, faculty = ?, department = ?, graduation = ?, academic_degree = ?, position = ? WHERE email = ?",
				profile.getLast_name(), profile.getFirst_name(), profile.getKana_last_name(), profile.getKana_first_name(),
				profile.getDate_of_birth(), profile.getSex(), profile.getPhone_number(), profile.getMajor(), profile.getUniversity(),
				profile.getFaculty(), profile.getDepartment(), profile.getGraduation(), profile.getAcademic_degree(), positions, profile.getEmail());
	}

}
