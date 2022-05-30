package tokyo.t6sdl.dancerscareer.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import tokyo.t6sdl.dancerscareer.model.Es;
import tokyo.t6sdl.dancerscareer.model.Experience;
import tokyo.t6sdl.dancerscareer.model.Interview;
import tokyo.t6sdl.dancerscareer.repository.ExperienceRepository;

@RequiredArgsConstructor
@Repository
public class JdbcExperienceRepository implements ExperienceRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final List<List<String>> SORT_LIST = Arrays.asList(Arrays.asList("id DESC"), Arrays.asList("kana_family_name ASC", "kana_given_name ASC", "id DESC"), Arrays.asList("univ_loc ASC", "univ_name ASC", "univ_fac ASC", "univ_dep ASC", "id DESC"));

	@Override
	public Experience findOneById(int id, boolean all, boolean pvCount) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			Experience exp = jdbcTemplate.queryForObject("SELECT * FROM experiences WHERE id = :id", params, (resultSet, i) -> {
					Experience experience = new Experience();
					return this.adjustToExp(experience, resultSet);
				});
			if (all) {
				exp.setEs(jdbcTemplate.query("SELECT * FROM es WHERE exp_id = :id ORDER BY corp ASC", params, (esSet, i) -> {
					Es es = new Es();
					return this.adjustToEs(es, esSet);
				}));
				exp.setInterview(jdbcTemplate.query("SELECT * FROM interview WHERE exp_id = :id ORDER BY id ASC", params, (itvSet, i) -> {
					Interview itv = new Interview();
					return this.adjustToItv(itv, itvSet);
				}));
			}
			if (pvCount) {
				jdbcTemplate.update("UPDATE experiences SET page_view = page_view + 1 WHERE id = :id", params);
			}
			return exp;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Experience findALittleOneById(int id) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			Experience exp = jdbcTemplate.queryForObject("SELECT * FROM experiences WHERE id = :id", params, (resultSet, i) -> {
					Experience experience = new Experience();
					return this.adjustToExp(experience, resultSet);
				});
			exp.setEs(jdbcTemplate.query("SELECT exp_id, id, corp, result, question, CONCAT(LEFT(answer, 200), '...') AS answer, advice FROM es WHERE exp_id = :id LIMIT 1", params, (esSet, i) -> {
				Es es = new Es();
				return this.adjustToEs(es, esSet);
			}));
			exp.setInterview(jdbcTemplate.query("SELECT * FROM interview WHERE exp_id = :id LIMIT 1", params, (itvSet, i) -> {
				Interview itv = new Interview();
				return this.adjustToItv(itv, itvSet);
			}));
			return exp;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Map<String, Object> find(int sort) {
		try {
			List<String> order = this.SORT_LIST.get(sort);
			return this.findExperiences(null, order, null);
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
			return this.findExperiences(target, order, params);
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
			return this.findExperiences(target, order, params);
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
			return this.findExperiences(target, order, params);
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
			return this.findExperiences(target, order, params);
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
			return this.findExperiences(target, order, params);
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
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pos", position);
		List<Integer> ids;
		if (andSearch) {
			String sql = "SELECT exp_id FROM senior_positions WHERE position IN (:pos) GROUP BY exp_id HAVING COUNT(exp_id) = :size ORDER BY exp_id DESC";
			params.put("size", position.size());
			ids = jdbcTemplate.queryForList(sql, params, Integer.class);
		} else {
			String sql = "SELECT exp_id FROM senior_positions WHERE position IN (:pos) GROUP BY exp_id ORDER BY exp_id DESC";
			ids = jdbcTemplate.queryForList(sql, params, Integer.class);
		}
		try {
			List<String> target = Arrays.asList(!ids.isEmpty() ? "id IN (:ids)" : "id = NULL");
			List<String> order = this.SORT_LIST.get(sort);
			params.clear();
			params.put("ids", ids);
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByCreatedAt() {
		List<String> target = Arrays.asList("created_at > CURRENT_TIMESTAMP - INTERVAL 1 WEEK");
		List<String> order = this.SORT_LIST.get(0);
		return this.findExperiences(target, order, null);
	}

	@Override
	public Es findEsById(int expId, int id) {
		try {
			String sql = "SELECT * FROM es WHERE exp_id = :expId AND id = :id";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("expId", expId);
			params.put("id", id);
			return jdbcTemplate.queryForObject(sql, params, (resultSet, i) -> {
					Es es = new Es();
					return this.adjustToEs(es, resultSet);
				});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public Interview findInterviewById(int expId, int id) {
		try {
			String sql = "SELECT * FROM interview WHERE exp_id = :expId AND id = :id";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("expId", expId);
			params.put("id", id);
			return jdbcTemplate.queryForObject(sql, params, (resultSet, i) -> {
					Interview itv = new Interview();
					return this.adjustToItv(itv, resultSet);
				});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insert(Experience newExp) {
		String sql = "INSERT INTO experiences VALUES (NULL, 0, 0, :fN, :giN, :kFN, :kGN, :sex, :mjr, :uL, :uN, :uF, :uD, :gL, :grN, :gS, :gD, :gI, :deg, :pos, :clb, :ofr, CURRENT_TIMESTAMP)";
		String pos = newExp.getPosition().stream().collect(Collectors.joining(","));
		String clb = newExp.getClub().stream().collect(Collectors.joining(","));
		String ofr = newExp.getOffer().stream().collect(Collectors.joining(","));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fN", newExp.getFamilyName());
		params.put("giN", newExp.getGivenName());
		params.put("kFN", newExp.getKanaFamilyName());
		params.put("kGN", newExp.getKanaGivenName());
		params.put("sex", newExp.getSex());
		params.put("mjr", newExp.getMajor());
		params.put("uL", newExp.getUnivLoc());
		params.put("uN", newExp.getUnivName());
		params.put("uF", newExp.getUnivFac());
		params.put("uD", newExp.getUnivDep());
		params.put("gL", newExp.getGradLoc());
		params.put("grN", newExp.getGradName());
		params.put("gS", newExp.getGradSchool());
		params.put("gD", newExp.getGradDiv());
		params.put("gI", newExp.getGraduatedIn());
		params.put("deg", newExp.getDegree());
		params.put("pos", pos);
		params.put("clb", clb);
		params.put("ofr", ofr);
		jdbcTemplate.update(sql, params);
		Integer id = jdbcTemplate.getJdbcTemplate().queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
		if (!newExp.getPosition().contains("")) {
			List<Map<String, Object>> paramMaps = new ArrayList<Map<String, Object>>();
			for (String p : newExp.getPosition()) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("position", p);
				paramMap.put("expId", id);
				paramMaps.add(paramMap);
			}
			jdbcTemplate.batchUpdate("INSERT INTO senior_positions VALUES (:position, :expId)", paramMaps.toArray(new Map[paramMaps.size()]));
		}
		if (!newExp.getEs().get(0).toString().isEmpty()) {
			List<Map<String, Object>> paramMaps = new ArrayList<Map<String, Object>>();
			int i = 0;
			for (Es es : newExp.getEs()) {
				i++;
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("expId", id);
				paramMap.put("id", i);
				paramMap.put("corp", es.getCorp());
				paramMap.put("result", es.getResult());
				paramMap.put("question", es.getQuestion());
				paramMap.put("answer", es.getAnswer());
				paramMap.put("advice", es.getAdvice());
				paramMaps.add(paramMap);
			}
			jdbcTemplate.batchUpdate("INSERT INTO es VALUES (:expId, :id, :corp, :result, :question, :answer, :advice)", paramMaps.toArray(new Map[paramMaps.size()]));
		}
		if (!(newExp.getInterview().get(0).toString().isEmpty())) {
			List<Map<String, Object>> paramMaps = new ArrayList<Map<String, Object>>();
			int i = 0;
			for (Interview itv : newExp.getInterview()) {
				i++;
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("expId", id);
				paramMap.put("id", i);
				paramMap.put("question", itv.getQuestion());
				paramMap.put("answer", itv.getAnswer());
				paramMaps.add(paramMap);
			}
			jdbcTemplate.batchUpdate("INSERT INTO interview VALUES (:expId, :id, :question, :answer)", paramMaps.toArray(new Map[paramMaps.size()]));
		}
		jdbcTemplate.getJdbcTemplate().update("UPDATE counts SET count = count + 1 WHERE name = 'experiences'");
	}

	@Override
	public void delete(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		jdbcTemplate.update("DELETE FROM senior_positions WHERE exp_id = :id", params);
		jdbcTemplate.update("DELETE FROM es WHERE exp_id = :id", params);
		jdbcTemplate.update("DELETE FROM interview WHERE exp_id = :id", params);
		jdbcTemplate.update("DELETE FROM experiences WHERE id = :id", params);
		params.clear();
		params.put("name", "experiences");
		jdbcTemplate.update("UPDATE counts SET count = CASE WHEN count = 0 THEN 0 ELSE count - 1 END WHERE name = :name", params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Experience exp) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", exp.getId());
		List<String> oldPos = jdbcTemplate.queryForList("SELECT position FROM senior_positions WHERE exp_id = :id", params, String.class);
		List<String> newPos = exp.getPosition();
		params.put("fN", exp.getFamilyName());
		params.put("giN", exp.getGivenName());
		params.put("kFN", exp.getKanaFamilyName());
		params.put("kGN", exp.getKanaGivenName());
		params.put("sex", exp.getSex());
		params.put("mjr", exp.getMajor());
		params.put("uL", exp.getUnivLoc());
		params.put("uN", exp.getUnivName());
		params.put("uF", exp.getUnivFac());
		params.put("uD", exp.getUnivDep());
		params.put("gL", exp.getGradLoc());
		params.put("grN", exp.getGradName());
		params.put("gS", exp.getGradSchool());
		params.put("gD", exp.getGradDiv());
		params.put("gI", exp.getGraduatedIn());
		params.put("deg", exp.getDegree());
		params.put("pos", newPos.stream().collect(Collectors.joining(",")));
		params.put("clb", exp.getClub().stream().collect(Collectors.joining(",")));
		params.put("ofr", exp.getOffer().stream().collect(Collectors.joining(",")));
		String sql = "UPDATE experiences SET "
				+ "family_name = :fN, "
				+ "given_name = :giN, "
				+ "kana_family_name = :kFN, "
				+ "kana_given_name = :kGN, "
				+ "sex = :sex, "
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
				+ "position = :pos, "
				+ "club = :clb, "
				+ "offer = :ofr "
				+ "WHERE id = :id";
		jdbcTemplate.update(sql, params);
		List<String> duplication = new ArrayList<String>();
		oldPos.forEach(e -> {
			if (newPos.contains(e)) duplication.add(e);
		});
		oldPos.removeAll(duplication);
		newPos.removeAll(duplication);
		if (!oldPos.isEmpty()) {
			params.clear();
			params.put("id", exp.getId());
			params.put("oldPos", oldPos);
			jdbcTemplate.update("DELETE FROM senior_positions WHERE position IN (:oldPos) AND exp_id = :id", params);
		}
		if (!newPos.isEmpty()) {
			List<Map<String, Object>> paramMaps = new ArrayList<Map<String, Object>>();
			for (String p : newPos) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("position", p);
				paramMap.put("expId", (Integer) exp.getId());
				paramMaps.add(paramMap);
			}
			jdbcTemplate.batchUpdate("INSERT INTO senior_positions VALUES (:position, :expId)", paramMaps.toArray(new Map[paramMaps.size()]));
		}

	}

	@Override
	public void updateLikes(int id, boolean increment) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Integer likes = jdbcTemplate.queryForObject("SELECT likes FROM experiences WHERE id = :id", params, Integer.class);
		if (increment) {
			jdbcTemplate.update("UPDATE experiences SET likes = likes + 1 WHERE id = :id", params);
		} else if (likes > 0) {
			jdbcTemplate.update("UPDATE experiences SET likes = likes - 1 WHERE id = :id", params);
		} else {
			jdbcTemplate.update("UPDATE experiences SET likes = 0 WHERE id = :id", params);
		}
	}

	@Override
	public void insertEs(Es newEs) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expId", newEs.getExpId());
		Integer id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM es WHERE exp_id = :expId", params, Integer.class);
		if (Objects.equals(id, null)) id = 0;
		params.put("id", id + 1);
		params.put("corp", newEs.getCorp());
		params.put("result", newEs.getResult());
		params.put("question", newEs.getQuestion());
		params.put("answer", newEs.getAnswer());
		params.put("advice", newEs.getAdvice());
		jdbcTemplate.update("INSERT INTO es VALUES (:expId, :id, :corp, :result, :question, :answer, :advice)", params);
	}

	@Override
	public void deleteEs(int expId, int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expId", expId);
		params.put("id", id);
		jdbcTemplate.update("DELETE FROM es WHERE exp_id = :expId AND id = :id", params);
	}

	@Override
	public void updateEs(Es es) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expId", es.getExpId());
		params.put("id", es.getId());
		params.put("corp", es.getCorp());
		params.put("result", es.getResult());
		params.put("question", es.getQuestion());
		params.put("answer", es.getAnswer());
		params.put("advice", es.getAdvice());
		jdbcTemplate.update("UPDATE es SET corp = :corp, result = :result, question = :question, answer = :answer, advice = :advice WHERE exp_id = :expId AND id = :id", params);
	}

	@Override
	public void insertInterview(Interview newItv) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expId", newItv.getExpId());
		Integer id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM interview WHERE exp_id = :expId", params, Integer.class);
		if (Objects.equals(id, null)) id = 0;
		params.put("id", id + 1);
		params.put("question", newItv.getQuestion());
		params.put("answer", newItv.getAnswer());
		jdbcTemplate.update("INSERT INTO interview VALUES (:expId, :id, :question, :answer)", params);
	}

	@Override
	public void deleteInterview(int expId, int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expId", expId);
		params.put("id", id);
		jdbcTemplate.update("DELETE FROM interview WHERE exp_id = :expId AND id = :id", params);
	}

	@Override
	public void updateInterview(Interview itv) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("expId", itv.getExpId());
		params.put("id", itv.getId());
		params.put("question", itv.getQuestion());
		params.put("answer", itv.getAnswer());
		jdbcTemplate.update("UPDATE interview SET question = :question, answer = :answer WHERE exp_id = :expId AND id = :id", params);
	}

	private Experience adjustToExp(Experience exp, ResultSet resultSet) throws SQLException {
		exp.setId(resultSet.getInt("id"));
		exp.setPageView(resultSet.getInt("page_view"));
		exp.setLikes(resultSet.getInt("likes"));
		exp.setFamilyName(resultSet.getString("family_name"));
		exp.setGivenName(resultSet.getString("given_name"));
		exp.setKanaFamilyName(resultSet.getString("kana_family_name"));
		exp.setKanaGivenName(resultSet.getString("kana_given_name"));
		exp.setSex(resultSet.getString("sex"));
		exp.setMajor(resultSet.getString("major"));
		exp.setUnivLoc(resultSet.getString("univ_loc"));
		exp.setUnivName(resultSet.getString("univ_name"));
		exp.setUnivFac(resultSet.getString("univ_fac"));
		exp.setUnivDep(resultSet.getString("univ_dep"));
		exp.setGradLoc(resultSet.getString("grad_loc"));
		exp.setGradName(resultSet.getString("grad_name"));
		exp.setGradSchool(resultSet.getString("grad_school"));
		exp.setGradDiv(resultSet.getString("grad_div"));
		exp.setGraduatedIn(resultSet.getString("graduated_in"));
		exp.setDegree(resultSet.getString("degree"));
		exp.setPosition(new ArrayList<String>(Arrays.asList(resultSet.getString("position").split(","))));
		exp.setClub(new ArrayList<String>(Arrays.asList(resultSet.getString("club").split(","))));
		exp.setOffer(new ArrayList<String>(Arrays.asList(resultSet.getString("offer").split(","))));
		return exp;
	}

	private Es adjustToEs(Es es, ResultSet resultSet) throws SQLException {
		es.setExpId(resultSet.getInt("exp_id"));
		es.setId(resultSet.getInt("id"));
		es.setCorp(resultSet.getString("corp"));
		es.setResult(resultSet.getString("result"));
		es.getQuestion().add(resultSet.getString("question"));
		es.getAnswer().add(resultSet.getString("answer"));
		es.getAdvice().add(resultSet.getString("advice"));
		return es;
	}

	private Interview adjustToItv(Interview itv, ResultSet resultSet) throws SQLException {
		itv.setExpId(resultSet.getInt("exp_id"));
		itv.setId(resultSet.getInt("id"));
		itv.setQuestion(resultSet.getString("question"));
		itv.setAnswer(resultSet.getString("answer"));
		return itv;
	}

	private Map<String, Object> findExperiences(List<String> target, List<String> order, Map<String, Object> params) {
		List<String> elements = new LinkedList<String>(Arrays.asList("SELECT", "COUNT(id)", "FROM experiences"));
		if (!Objects.equals(target, null) && !target.isEmpty()) elements.addAll(Arrays.asList("WHERE", target.stream().collect(Collectors.joining(" AND "))));
		String sql = elements.stream().collect(Collectors.joining(" "));
		Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
		elements.set(1, "*");
		if (!Objects.equals(order, null) && !order.isEmpty()) elements.addAll(Arrays.asList("ORDER BY", order.stream().collect(Collectors.joining(", "))));
		sql = elements.stream().collect(Collectors.joining(" "));
		List<Experience> experiences = jdbcTemplate.query(sql, params, (resultSet, i) -> {
				Experience experience = new Experience();
				return this.adjustToExp(experience, resultSet);
			});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}
}
