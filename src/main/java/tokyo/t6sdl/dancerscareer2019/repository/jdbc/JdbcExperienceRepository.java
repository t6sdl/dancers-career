package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;
import tokyo.t6sdl.dancerscareer2019.repository.ExperienceRepository;

@RequiredArgsConstructor
@Repository
public class JdbcExperienceRepository implements ExperienceRepository {
	private final JdbcTemplate jdbcTemplate;
	
	private List<String> stringToList(String str) {
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(str.split(","));
		return list;
	}
	
	private String listToString(List<String> list) {
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
	public List<Experience> find() {
		return jdbcTemplate.query(
				"SELECT * FROM experiences ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				});
	}

	@Override
	public List<Experience> findByName(String kanaLastName, String kanaFirstName) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE kana_last_name = ? AND kana_first_name = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				}, kanaLastName, kanaFirstName);
	}

	@Override
	public List<Experience> findByLastName(String kanaLastName) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE kana_last_name = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				}, kanaLastName);
	}

	@Override
	public List<Experience> findByPrefecture(String prefecture) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				}, prefecture);
	}

	@Override
	public List<Experience> findByUniversity(String prefecture, String university) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? AND university = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				}, prefecture, university);
	}

	@Override
	public List<Experience> findByFaculty(String prefecture, String university, String faculty) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? AND university = ? AND faculty = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				}, prefecture, university, faculty);
	}

	@Override
	public List<Experience> findByDepartment(String prefecture, String university, String faculty, String department) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? AND university = ? AND faculty = ? AND department = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				}, prefecture, university, faculty, department);
	}

	@Override
	public List<Experience> findByPosition(List<String> position, String method) {
		StringBuffer like = new StringBuffer();
		for (int i = 0; i < position.size(); i++) {
			like.append("position LIKE '%").append(position.get(i)).append("%'");
			if (i < position.size() - 1) {
				like.append(" " + method + " ");
			}
		}
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE " + like + " ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet);
					return experience;
				});
	}

	@Override
	public void insert(Experience newExperience) {
		String position = this.listToString(newExperience.getPosition());
		String club = this.listToString(newExperience.getClub());
		String offer = this.listToString(newExperience.getOffer());
		jdbcTemplate.update(
				"INSERT INTO experiences (last_name, first_name, kana_last_name, kana_first_name, sex, major, prefecture, university, faculty, department, graduation, academic_degree, position, club, offer) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				newExperience.getLast_name(), newExperience.getFirst_name(), newExperience.getKana_last_name(), newExperience.getKana_first_name(), newExperience.getSex(), newExperience.getMajor(), 
				newExperience.getPrefecture(), newExperience.getUniversity(), newExperience.getFaculty(), newExperience.getDepartment(), newExperience.getGraduation(), newExperience.getAcademic_degree(), position, club, offer);
		Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		if (!(newExperience.getEs().get(0).toString().isEmpty())) {
			List<Es> es = newExperience.getEs();
			for (int i = 0; i < es.size(); i++) {
				jdbcTemplate.update(
						"INSERT INTO es VALUES (?, ?, ?, ?, ?, ?, ?)",
						id, i + 1, es.get(i).getCorp(), es.get(i).getResult(), es.get(i).getQuestion(), es.get(i).getAnswer(), es.get(i).getAdvice());
			}
		}
		if (!(newExperience.getInterview().get(0).toString().isEmpty())) {
			List<Interview> interview = newExperience.getInterview();
			for (int j = 0; j < interview.size(); j++) {
				jdbcTemplate.update(
						"INSERT INTO interview VALUES (?, ?, ?, ?)",
						id, j + 1, interview.get(j).getQuestion(), interview.get(j).getAnswer());
			}
		}
	}

	@Override
	public void delete(int experience_id) {
		jdbcTemplate.update("DELETE FROM es WHERE experience_id = ?", experience_id);
		jdbcTemplate.update("DELETE FROM interview WHERE experience_id = ?", experience_id);
		jdbcTemplate.update("DELETE FROM experiences WHERE experience_id = ?", experience_id);
	}

	@Override
	public void update(Experience experience) {
		String position = this.listToString(experience.getPosition());
		String club = this.listToString(experience.getClub());
		String offer = this.listToString(experience.getOffer());
		jdbcTemplate.update(
				"UPDATE experiences SET last_name = ?, first_name = ?, kana_last_name = ?, kana_first_name = ?, sex = ?, major = ?, "
				+ "prefecture = ?, university = ?, faculty = ?, department = ?, graduation = ?, academic_degree = ?, position = ?, club = ?, offer = ?",
				experience.getLast_name(), experience.getFirst_name(), experience.getKana_last_name(), experience.getKana_first_name(), experience.getSex(), experience.getMajor(), 
				experience.getPrefecture(), experience.getUniversity(), experience.getFaculty(), experience.getDepartment(), experience.getGraduation(), experience.getAcademic_degree(), position, club, offer);
	}

	@Override
	public void insertEs(Es newEs) {
		jdbcTemplate.update(
				"INSERT INTO es VALUES (?, ?, ?, ?, ?, ?, ?)",
				newEs.getExperience_id(), newEs.getEs_id(), newEs.getCorp(), newEs.getResult(), newEs.getQuestion(), newEs.getAnswer(), newEs.getAdvice());
	}

	@Override
	public void deleteEs(int experience_id, int es_id) {
		jdbcTemplate.update("DELETE FROM es WHERE experience_id = ? AND es_id = ?", experience_id, es_id);
	}

	@Override
	public void updateEs(Es es) {
		jdbcTemplate.update(
				"UPDATE es SET corp = ?, result = ?, question = ?, answer = ?, advice = ? WHERE experience_id = ? AND es_id = ?",
				es.getCorp(), es.getResult(), es.getQuestion(), es.getAnswer(), es.getAdvice(), es.getExperience_id(), es.getEs_id());
	}

	@Override
	public void insertInterview(Interview newInterview) {
		jdbcTemplate.update(
				"INSERT INTO interview VALUES (?, ?, ?, ?)",
				newInterview.getExperience_id(), newInterview.getInterview_id(), newInterview.getQuestion(), newInterview.getAnswer());
	}

	@Override
	public void deleteInterview(int experience_id, int interview_id) {
		jdbcTemplate.update("DELETE FROM interview WHERE experience_id = ? AND interview_id = ?", experience_id, interview_id);
	}

	@Override
	public void updateInterview(Interview interview) {
		jdbcTemplate.update(
				"UPDATE interview SET question = ?, answer = ? WHERE experience_id = ? AND interview_id = ?",
				interview.getQuestion(), interview.getAnswer(), interview.getExperience_id(), interview.getInterview_id());
	}

	private void adjustDataToExperience(Experience experience, ResultSet resultSet) throws SQLException {
		experience.setExperience_id(resultSet.getInt("experience_id"));
		experience.setLast_name(resultSet.getString("last_name"));
		experience.setFirst_name(resultSet.getString("first_name"));
		experience.setKana_last_name(resultSet.getString("kana_last_name"));
		experience.setKana_first_name(resultSet.getString("kana_first_name"));
		experience.setSex(resultSet.getString("sex"));
		experience.setMajor(resultSet.getString("major"));
		experience.setPrefecture(resultSet.getString("prefecture"));
		experience.setUniversity(resultSet.getString("university"));
		experience.setFaculty(resultSet.getString("faculty"));
		experience.setDepartment(resultSet.getString("department"));
		experience.setGraduation(resultSet.getString("graduation"));
		experience.setAcademic_degree(resultSet.getString("academic_degree"));
		experience.setPosition(this.stringToList(resultSet.getString("position")));
		experience.setClub(this.stringToList(resultSet.getString("club")));
		experience.setOffer(this.stringToList(resultSet.getString("offer")));
		experience.setEs(jdbcTemplate.query(
				"SELECT * FROM es WHERE experience_id = ?", (esSet, j) -> {
					Es es = new Es();
					es.setExperience_id(experience.getExperience_id());
					es.setEs_id(esSet.getInt("es_id"));
					es.setCorp(esSet.getString("corp"));
					es.setResult(esSet.getString("result"));
					es.setQuestion(esSet.getString("question"));
					es.setAnswer(esSet.getString("answer"));
					es.setAdvice(esSet.getString("advice"));
					return es;
				}, experience.getExperience_id()));
		experience.setInterview(jdbcTemplate.query(
				"SELECT * FROM interview WHERE experience_id = ?", (interviewSet, k) -> {
					Interview interview = new Interview();
					interview.setExperience_id(experience.getExperience_id());
					interview.setInterview_id(interviewSet.getInt("interview_id"));
					interview.setQuestion(interviewSet.getString("question"));
					interview.setAnswer(interviewSet.getString("answer"));
					return interview;
				}, experience.getExperience_id()));					
	}
}
