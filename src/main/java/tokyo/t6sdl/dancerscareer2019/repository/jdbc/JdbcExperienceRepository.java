package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.dao.EmptyResultDataAccessException;
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
	private final String QUERIED_VALUE = "experience_id, page_view, likes, last_name, first_name, kana_last_name, kana_first_name, sex, major, prefecture, university, faculty, department, graduation, academic_degree, club, offer";
	private final String POSITION = "GROUP_CONCAT(CONCAT('[', position, ']') SEPARATOR ',') AS position";
	private final String ES = "GROUP_CONCAT(CONCAT('[', es_id, '],'), CONCAT('[', corp, '],'), CONCAT('[', result, '],'), CONCAT('[', es.question, '],'), CONCAT('[', es.answer, '],'), CONCAT('[', advice, ']') SEPARATOR ',') AS es";
	private final String INTERVIEW = "GROUP_CONCAT(CONCAT('[', interview_id, '],'), CONCAT('[', interview.question, '],'), CONCAT('[', interview.answer, ']') SEPARATOR ',') AS interview";

	private List<String> stringToList(String str) {
		List<String> list = new ArrayList<String>();
		Arrays.asList(str.split(",")).forEach(each -> {
			if (each.startsWith("[") && each.endsWith("]")) {
				StringBuilder sb = new StringBuilder(each);
				each = sb.substring(1, sb.length() - 1);
			}
			list.add(each);
		});;
		return list;
	}
	
	private String listToString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append("[").append(list.get(i)).append("]");
			if (i < list.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	@Override
	public Experience findOneById(int experience_id, boolean all, boolean pv_count) {
		try {
			if (pv_count) {
				jdbcTemplate.update("UPDATE experiences SET page_view = page_view + 1 WHERE experience_id = ?", experience_id);
			}
			return jdbcTemplate.queryForObject(
					this.selectExperienceIn("WHERE experience_id = ?", all, false), (resultSet, i) -> {
						Experience experience = new Experience();
						this.adjustDataToExperience(experience, resultSet, all);
						return experience;
					}, experience_id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> find() {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM experiences", Integer.class);
		List<Experience> experiences = jdbcTemplate.query(
				this.selectExperienceIn("", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}

	@Override
	public Map<String, Object> findByName(String kanaLastName, String kanaFirstName) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM experiences WHERE kana_last_name = ? AND kana_first_name = ?", Integer.class, kanaLastName, kanaFirstName);
		List<Experience> experiences = jdbcTemplate.query(
				this.selectExperienceIn("WHERE kana_last_name = ? AND kana_first_name = ?", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, kanaLastName, kanaFirstName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}

	@Override
	public Map<String, Object> findByLastName(String kanaLastName) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM experiences WHERE kana_last_name = ?", Integer.class, kanaLastName);
		List<Experience> experiences = jdbcTemplate.query(
				this.selectExperienceIn("WHERE kana_last_name = ?", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, kanaLastName);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}

	@Override
	public Map<String, Object> findByPrefecture(String prefecture) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM experiences WHERE prefecture = ?", Integer.class, prefecture);
		List<Experience> experiences = jdbcTemplate.query(
				this.selectExperienceIn("WHERE prefecture = ?", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}

	@Override
	public Map<String, Object> findByUniversity(String prefecture, String university) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM experiences WHERE prefecture = ? AND university = ?", Integer.class, prefecture, university);
		List<Experience> experiences = jdbcTemplate.query(
				this.selectExperienceIn("WHERE prefecture = ? AND university = ?", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture, university);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}

	@Override
	public Map<String, Object> findByFaculty(String prefecture, String university, String faculty) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM experiences WHERE prefecture = ? AND university = ? AND faculty = ?", Integer.class, prefecture, university, faculty);
		List<Experience> experiences = jdbcTemplate.query(
				this.selectExperienceIn("WHERE prefecture = ? AND university = ? AND faculty = ?", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture, university, faculty);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}

	@Override
	public Map<String, Object> findByDepartment(String prefecture, String university, String faculty, String department) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM experiences WHERE prefecture = ? AND university = ? AND faculty = ? AND department = ?", Integer.class, prefecture, university, faculty, department);
		List<Experience> experiences = jdbcTemplate.query(
				this.selectExperienceIn("WHERE prefecture = ? AND university = ? AND faculty = ? AND department = ?", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture, university, faculty, department);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}

	@Override
	public Map<String, Object> findByPosition(List<String> position, boolean andSearch) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", 0);
		result.put("experiences", null);
		if (position.contains("")) {
			return result;
		}
		StringBuilder posStr = new StringBuilder();
		for (int i = 0; i < position.size(); i++) {
			posStr.append("'" + position.get(i) + "'");
			if (i < position.size() - 1) {
				posStr.append(", ");
			}
		}
		List<Integer> ids = new ArrayList<Integer>();
		if (andSearch) {
			ids.addAll(jdbcTemplate.query(
					"SELECT id FROM senior_positions WHERE position IN (" + posStr.toString() + ") GROUP BY id HAVING COUNT(id) = ? ORDER BY id DESC", (resultSet, i) -> {
						return resultSet.getInt("id");
					}, position.size()));
		} else {
			ids.addAll(jdbcTemplate.query(
					"SELECT id FROM senior_positions WHERE position IN (" + posStr.toString() + ") GROUP BY id ORDER BY id DESC", (resultSet, i) -> {
						return resultSet.getInt("id");
					}));
		}
		if (Objects.equals(ids, null) || ids.isEmpty()) {
			return result;
		}
		List<Experience> experiences = this.findById(ids);
		result.replace("count", ids.size());
		result.replace("experiences", experiences);
		return result;
	}

	@Override
	public Es findEsById(int experience_id, int es_id) {
		try {
			return jdbcTemplate.queryForObject(
					"SELECT * FROM es WHERE id = ? AND es_id = ?", (resultSet, i) -> {
						Es es = new Es();
						es.setExperience_id(resultSet.getInt("id"));
						es.setEs_id(resultSet.getInt("es_id"));
						es.setCorp(resultSet.getString("corp"));
						es.setResult(resultSet.getString("result"));
						es.setQuestion(resultSet.getString("question"));
						es.setAnswer(resultSet.getString("answer"));
						es.setAdvice(resultSet.getString("advice"));
						return es;
					}, experience_id, es_id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public Interview findInterviewById(int experience_id, int interview_id) {
		try {
			return jdbcTemplate.queryForObject(
					"SELECT * FROM interview WHERE id = ? AND interview_id = ?", (resultSet, i) -> {
						Interview interview = new Interview();
						interview.setExperience_id(resultSet.getInt("id"));
						interview.setInterview_id(resultSet.getInt("interview_id"));
						interview.setQuestion(resultSet.getString("question"));
						interview.setAnswer(resultSet.getString("answer"));
						return interview;
					}, experience_id, interview_id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void insert(Experience newExperience) {
		String club = this.listToString(newExperience.getClub());
		String offer = this.listToString(newExperience.getOffer());
		jdbcTemplate.update(
				"INSERT INTO experiences (last_name, first_name, kana_last_name, kana_first_name, sex, major, prefecture, university, faculty, department, graduation, academic_degree, club, offer) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				newExperience.getLast_name(), newExperience.getFirst_name(), newExperience.getKana_last_name(), newExperience.getKana_first_name(), newExperience.getSex(), newExperience.getMajor(), 
				newExperience.getPrefecture(), newExperience.getUniversity(), newExperience.getFaculty(), newExperience.getDepartment(), newExperience.getGraduation(), newExperience.getAcademic_degree(), club, offer);
		Integer id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		if (!(newExperience.getPosition().contains(""))) {
			newExperience.getPosition().forEach(position -> {
				jdbcTemplate.update("INSERT INTO senior_positions VALUES (?, ?)", newExperience.getExperience_id(), position);
			});
		}
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
		jdbcTemplate.update("DELETE FROM senior_positions WHERE id = ?", experience_id);
		jdbcTemplate.update("DELETE FROM es WHERE id = ?", experience_id);
		jdbcTemplate.update("DELETE FROM interview WHERE id = ?", experience_id);
		jdbcTemplate.update("DELETE FROM experiences WHERE experience_id = ?", experience_id);
	}

	@Override
	public void update(Experience experience) {
		String club = this.listToString(experience.getClub());
		String offer = this.listToString(experience.getOffer());
		jdbcTemplate.update(
				"UPDATE experiences SET last_name = ?, first_name = ?, kana_last_name = ?, kana_first_name = ?, sex = ?, major = ?, "
				+ "prefecture = ?, university = ?, faculty = ?, department = ?, graduation = ?, academic_degree = ?, club = ?, offer = ? WHERE experience_id = ?",
				experience.getLast_name(), experience.getFirst_name(), experience.getKana_last_name(), experience.getKana_first_name(), experience.getSex(), experience.getMajor(), 
				experience.getPrefecture(), experience.getUniversity(), experience.getFaculty(), experience.getDepartment(), experience.getGraduation(), experience.getAcademic_degree(), club, offer, experience.getExperience_id());
		jdbcTemplate.update("DELETE FROM senior_positions WHERE id = ?", experience.getExperience_id());
		if (!(experience.getPosition().contains(""))) {
			experience.getPosition().forEach(position -> {
				jdbcTemplate.update("INSERT INTO senior_positions VALUES (?, ?)", experience.getExperience_id(), position);
			});
		}

	}
	
	@Override
	public void updateLikes(int experience_id, boolean increment) {
		Integer likes = jdbcTemplate.queryForObject("SELECT (likes) FROM experiences WHERE experience_id = ?", Integer.class, experience_id);
		if (increment) {
			jdbcTemplate.update("UPDATE experiences SET likes = likes + 1 WHERE experience_id = ?", experience_id);
		} else if (likes > 0) {
			jdbcTemplate.update("UPDATE experiences SET likes = likes - 1 WHERE experience_id = ?", experience_id);
		} else {
			jdbcTemplate.update("UPDATE experiences SET likes = 0 WHERE experience_id = ?", experience_id);
		}
	}

	@Override
	public void insertEs(Es newEs) {
		Integer es_id = jdbcTemplate.queryForObject("SELECT MAX(es_id) FROM es WHERE id = ?", Integer.class, newEs.getExperience_id());
		if (Objects.equals(es_id, null)) {
			es_id = 0;
		}
		jdbcTemplate.update(
				"INSERT INTO es VALUES (?, ?, ?, ?, ?, ?, ?)",
				newEs.getExperience_id(), es_id + 1, newEs.getCorp(), newEs.getResult(), newEs.getQuestion(), newEs.getAnswer(), newEs.getAdvice());
	}

	@Override
	public void deleteEs(int experience_id, int es_id) {
		jdbcTemplate.update("DELETE FROM es WHERE id = ? AND es_id = ?", experience_id, es_id);
	}

	@Override
	public void updateEs(Es es) {
		jdbcTemplate.update(
				"UPDATE es SET corp = ?, result = ?, question = ?, answer = ?, advice = ? WHERE id = ? AND es_id = ?",
				es.getCorp(), es.getResult(), es.getQuestion(), es.getAnswer(), es.getAdvice(), es.getExperience_id(), es.getEs_id());
	}

	@Override
	public void insertInterview(Interview newInterview) {
		Integer interview_id = jdbcTemplate.queryForObject("SELECT MAX(interview_id) FROM interview WHERE id = ?", Integer.class, newInterview.getExperience_id());
		if (Objects.equals(interview_id, null)) {
			interview_id = 0;
		}
		jdbcTemplate.update(
				"INSERT INTO interview VALUES (?, ?, ?, ?)",
				newInterview.getExperience_id(), interview_id + 1, newInterview.getQuestion(), newInterview.getAnswer());
	}

	@Override
	public void deleteInterview(int experience_id, int interview_id) {
		jdbcTemplate.update("DELETE FROM interview WHERE id = ? AND interview_id = ?", experience_id, interview_id);
	}

	@Override
	public void updateInterview(Interview interview) {
		jdbcTemplate.update(
				"UPDATE interview SET question = ?, answer = ? WHERE id = ? AND interview_id = ?",
				interview.getQuestion(), interview.getAnswer(), interview.getExperience_id(), interview.getInterview_id());
	}

	protected List<Experience> findById(List<Integer> ids) {
		if (Objects.equals(ids, null) || ids.isEmpty()) {
			return null;
		}
		StringBuilder idsStr = new StringBuilder();
		for (int i = 0; i < ids.size(); i++) {
			idsStr.append(String.valueOf(ids.get(i)));
			if (i < ids.size() - 1) {
				idsStr.append(", ");
			}
		}
		return jdbcTemplate.query(
				this.selectExperienceIn("WHERE experience_id IN (" + idsStr.toString() + ")", false, true), (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				});
	}

	private void adjustDataToExperience(Experience experience, ResultSet resultSet, boolean all) throws SQLException {
		experience.setExperience_id(resultSet.getInt("experience_id"));
		experience.setPage_view(resultSet.getInt("page_view"));
		experience.setLikes(resultSet.getInt("likes"));
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
		experience.setClub(this.stringToList(resultSet.getString("club")));
		experience.setOffer(this.stringToList(resultSet.getString("offer")));
		experience.setPosition(this.stringToList(resultSet.getString("position")));
		if (all) {
			List<String> esData = this.stringToList(resultSet.getString("es"));
			List<Es> es = new ArrayList<Es>();
			for (int i = 0; i < esData.size() % 6; i++) {
				Es e = new Es();
				e.setExperience_id(experience.getExperience_id());
				for (int j = 0; j < 6; j++) {
					if (j % 6 == 0) {
						e.setEs_id(Integer.parseInt(esData.get(6 * i)));
					} else if (j % 6 == 1) {
						e.setCorp(esData.get(6 * i + 1));
					} else if (j % 6 == 2) {
						e.setResult(esData.get(6 * i + 2));
					} else if (j % 6 == 3) {
						e.setQuestion(esData.get(6 * i + 3));
					} else if (j % 6 == 4) {
						e.setAnswer(esData.get(6 * i + 4));
					} else if (j % 6 == 5) {
						e.setAdvice(esData.get(6 * i + 5));
					}
				}
				es.add(e);
			}
			experience.setEs(es);
			List<String> interviewData = this.stringToList(resultSet.getString("interview"));
			List<Interview> interview = new ArrayList<Interview>();
			for (int i = 0; i < interviewData.size() % 6; i++) {
				Interview in = new Interview();
				in.setExperience_id(experience.getExperience_id());
				for (int j = 0; j < 6; j++) {
					if (j % 6 == 0) {
						in.setInterview_id(Integer.parseInt(interviewData.get(6 * i)));
					} else if (j % 6 == 1) {
						in.setQuestion(interviewData.get(6 * i + 1));
					} else if (j % 6 == 2) {
						in.setAnswer(interviewData.get(6 * i + 2));
					}
				}
				interview.add(in);
			}
			experience.setInterview(interview);
		}
	}
	
	private String selectExperienceIn(String condition, boolean all, boolean multiple) {
		if (all) {
			return "SELECT " + this.QUERIED_VALUE + ", " + this.POSITION + ", " + this.ES + ", " + this.INTERVIEW + " FROM experiences LEFT OUTER JOIN senior_positions ON experience_id = senior_positions.id LEFT OUTER JOIN es ON experience_id = es.id LEFT OUTER JOIN interview ON experience_id = interview.id " + condition + " GROUP BY " + this.QUERIED_VALUE;
		} else if (multiple) {
			return "SELECT " + this.QUERIED_VALUE + ", " + this.POSITION + " FROM experiences LEFT OUTER JOIN senior_positions ON experience_id = senior_positions.id " + condition + " GROUP BY " + this.QUERIED_VALUE + " ORDER BY experience_id DESC";
		} else {
			return "SELECT " + this.QUERIED_VALUE + ", " + this.POSITION + " FROM experiences LEFT OUTER JOIN senior_positions ON experience_id = senior_positions.id " + condition + " GROUP BY " + this.QUERIED_VALUE;
		}
	}
}
