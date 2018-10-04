package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
	public List<Experience> find() {
		return jdbcTemplate.query(
				"SELECT * FROM experiences ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				});
	}
	
	@Override
	public Experience findOneById(int experience_id, boolean all, boolean pv_count) {
		try {
			if (pv_count) {
				jdbcTemplate.update("UPDATE experiences SET page_view = page_view + 1 WHERE experience_id = ?", experience_id);
			}
			return jdbcTemplate.queryForObject(
					"SELECT * FROM experiences WHERE experience_id = ?", (resultSet, i) -> {
						Experience experience = new Experience();
						this.adjustDataToExperience(experience, resultSet, all);
						return experience;
					}, experience_id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Experience> findByName(String kanaLastName, String kanaFirstName) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE kana_last_name = ? AND kana_first_name = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, kanaLastName, kanaFirstName);
	}

	@Override
	public List<Experience> findByLastName(String kanaLastName) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE kana_last_name = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, kanaLastName);
	}

	@Override
	public List<Experience> findByPrefecture(String prefecture) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture);
	}

	@Override
	public List<Experience> findByUniversity(String prefecture, String university) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? AND university = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture, university);
	}

	@Override
	public List<Experience> findByFaculty(String prefecture, String university, String faculty) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? AND university = ? AND faculty = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture, university, faculty);
	}

	@Override
	public List<Experience> findByDepartment(String prefecture, String university, String faculty, String department) {
		return jdbcTemplate.query(
				"SELECT * FROM experiences WHERE prefecture = ? AND university = ? AND faculty = ? AND department = ? ORDER BY experience_id DESC", (resultSet, i) -> {
					Experience experience = new Experience();
					this.adjustDataToExperience(experience, resultSet, false);
					return experience;
				}, prefecture, university, faculty, department);
	}

	@Override
	public List<Experience> findByPosition(List<String> position, String method) {
		if (position.contains("")) {
			return null;
		} else if (position.size() == 1) {
			method = "OR";
		}
		List<List<Integer>> results = new ArrayList<List<Integer>>();
		position.forEach(pos -> {
			List<Integer> result = jdbcTemplate.query(
					"SELECT (id) FROM senior_positions WHERE position = ? ORDER BY id DESC", (resultSet, i) -> {
						return resultSet.getInt("id");
					}, pos);
			if (!(Objects.equals(result, null))) {
				results.add(result);
			}
		});
		Set<Integer> ids = new HashSet<Integer>();
		switch (method) {
		case "OR":
			results.forEach(result -> {
				ids.addAll(result);
			});
			break;
		case "AND":
			results.get(0).forEach(id -> {
				boolean isRepeated = true;
				for (int i = 1; i < results.size(); i++) {
					if (!(results.get(i).contains(id))) {
						isRepeated = false;
						break;
					}
				}
				if (isRepeated) {
					ids.add(id);
				}
			});
			break;
		default:
			return null;
		}
		List<Experience> experiences = new ArrayList<Experience>();
		ids.forEach(id -> {
			experiences.add(this.findOneById(id, false, false));
		});
		return experiences;
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
		List<String> position = jdbcTemplate.query("SELECT (position) FROM senior_positions WHERE id = ?", (posSet, i) -> {
			return posSet.getString("position");
		}, experience.getExperience_id());
		experience.setPosition(position);
		if (all) {
			experience.setEs(jdbcTemplate.query(
					"SELECT * FROM es WHERE id = ? ORDER BY corp DESC", (esSet, j) -> {
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
					"SELECT * FROM interview WHERE id = ? ORDER BY interview_id ASC", (interviewSet, k) -> {
						Interview interview = new Interview();
						interview.setExperience_id(experience.getExperience_id());
						interview.setInterview_id(interviewSet.getInt("interview_id"));
						interview.setQuestion(interviewSet.getString("question"));
						interview.setAnswer(interviewSet.getString("answer"));
						return interview;
					}, experience.getExperience_id()));
		}
	}
}
