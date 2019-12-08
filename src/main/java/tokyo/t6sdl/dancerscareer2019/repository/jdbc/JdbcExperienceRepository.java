package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

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
import java.util.stream.IntStream;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;
import tokyo.t6sdl.dancerscareer2019.repository.ExperienceRepository;

@RequiredArgsConstructor
@Repository
public class JdbcExperienceRepository implements ExperienceRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final List<List<String>> SORT_LIST = Arrays.asList(Arrays.asList("id DESC"), Arrays.asList("kana_family_name ASC", "kana_given_name ASC"), Arrays.asList("univ_loc ASC", "univ_name ASC", "univ_fac ASC", "univ_dep ASC"));

	@Override
	public Experience findOneById(int id, boolean all, boolean pv_count) {
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
			if (pv_count) {
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
			List<String> target = Arrays.asList("kana_family_name = :kanaFN", "kana_given_name = :kanaGN");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("kanaFN", kanaFamilyName);
			params.put("kanaGN", kanaGivenName);
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByLastName(int sort, String kanaFamilyName) {
		try {
			List<String> target = Arrays.asList("kana_family_name = :kanaFN");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("kanaFN", kanaFamilyName);
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByPrefecture(int sort, String univLoc) {
		try {
			List<String> target = Arrays.asList("univ_loc = :univLoc");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("univLoc", univLoc);
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByUniversity(int sort, String univLoc, String univName) {
		try {
			List<String> target = Arrays.asList("univ_loc = :univLoc", "univ_name = :univName");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("univLoc", univLoc);
			params.put("univName", univName);
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByFaculty(int sort, String univLoc, String univName, String univFac) {
		try {
			List<String> target = Arrays.asList("univ_loc = :univLoc", "univ_name = :univName", "univ_fac = :univFac");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("univLoc", univLoc);
			params.put("univName", univName);
			params.put("univFac", univFac);
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByDepartment(int sort, String univLoc, String univName, String univFac, String univDep) {
		try {
			List<String> target = Arrays.asList("univ_loc = :univLoc", "univ_name = :univName", "univ_fac = :univFac", "univ_dep = :univDep");
			List<String> order = this.SORT_LIST.get(sort);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("univLoc", univLoc);
			params.put("univName", univName);
			params.put("univFac", univFac);
			params.put("univDep", univDep);
			return this.findExperiences(target, order, params);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Sort should be less than " + this.SORT_LIST.size());
		}
	}

	@Override
	public Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("positions", position);
		List<Integer> ids;
		if (andSearch) {
			String sql = "SELECT exp_id FROM senior_positions WHERE position IN :positions GROUP BY exp_id HAVING exp_id = :size ORDER BY exp_id DESC";
			params.put("size", position.size());
			ids = jdbcTemplate.query(sql, params, (resultSet, i) -> {
					return resultSet.getInt("exp_id");
				});
		} else {
			String sql = "SELECT exp_id FROM senior_positions WHERE position IN :positions GROUP BY exp_id ORDER BY exp_id DESC";
			ids = jdbcTemplate.query(sql, params, (resultSet, i) -> {
					return resultSet.getInt("exp_id");
				});
		}
		try {
			List<String> target = Arrays.asList("id IN :ids");
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

	@Override
	public void insert(Experience newExp) {
		String sql = "INSERT INTO experiences VALUES (NULL, 0, 0, :fN, :gN, :kFN, :kGN, :sex, :mjr, :uL, :uN, :uF, :uD, :gL, :gN, :gS, :gD, :gI, :deg, :pos, :clb, :ofr)";
		String pos = newExp.getPosition().stream().collect(Collectors.joining(","));
		String clb = newExp.getClub().stream().collect(Collectors.joining(","));
		String ofr = newExp.getOffer().stream().collect(Collectors.joining(","));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fN", newExp.getFamilyName());
		params.put("gN", newExp.getGivenName());
		params.put("kFN", newExp.getKanaFamilyName());
		params.put("kGN", newExp.getKanaGivenName());
		params.put("sex", newExp.getSex());
		params.put("mjr", newExp.getMajor());
		params.put("uL", newExp.getUnivLoc());
		params.put("uN", newExp.getUnivName());
		params.put("uF", newExp.getUnivFac());
		params.put("uD", newExp.getUnivDep());
		params.put("gL", newExp.getGradLoc());
		params.put("gN", newExp.getGradName());
		params.put("gS", newExp.getGradSchool());
		params.put("gD", newExp.getGradDiv());
		params.put("gI", newExp.getGraduatedIn());
		params.put("deg", newExp.getDegree());
		params.put("pos", pos);
		params.put("clb", clb);
		params.put("ofr", ofr);
		jdbcTemplate.update(sql, params);
		Integer id = jdbcTemplate.getJdbcTemplate().queryForObject("SELECT LAST_INSERT_ID() FROM experiences", Integer.class);
		if (!newExp.getPosition().contains("")) {
			List<SenPosParam> senPosParams = newExp.getPosition().stream().map(e -> new SenPosParam(id, e)).collect(Collectors.toList());
			params.clear();
			params.put("senPosParams", senPosParams);
			jdbcTemplate.update("INSERT INTO senior_positions VALUES :senPosParams", params);
		}
		if (!newExp.getEs().get(0).toString().isEmpty()) {
			List<Es> es = newExp.getEs();
			List<EsParam> esParams = IntStream.rangeClosed(1, es.size()).mapToObj(i -> new EsParam(id, i, es.get(i).getCorp(), es.get(i).getResult(), es.get(i).getQuestion().get(0), es.get(i).getAnswer().get(0), es.get(i).getAdvice().get(0))).collect(Collectors.toList());
			params.clear();
			params.put("esParams", esParams);
			jdbcTemplate.update("INSERT INTO es VALUES :esParams", params);
		}
		if (!(newExp.getInterview().get(0).toString().isEmpty())) {
			List<Interview> itv = newExp.getInterview();
			List<ItvParam> itvParams = IntStream.rangeClosed(1, itv.size()).mapToObj(i -> new ItvParam(id, i, itv.get(i).getQuestion(), itv.get(i).getAnswer())).collect(Collectors.toList());
			params.clear();
			params.put("itvParams", itvParams);
			jdbcTemplate.update("INSERT INTO interview VALUES :itvParams", params);
		}
		params.clear();
		params.put("name", "experiences");
		jdbcTemplate.update("UPDATE counts SET count = count + 1 WHERE name = :name", params);
	}
	
	private class SenPosParam {
		protected int exp_id;
		protected String position;
		
		public SenPosParam(int expId, String position) {
			this.exp_id = expId;
			this.position = position;
		}
	}
	
	private class EsParam {
		protected int exp_id;
		protected int id;
		protected String corp;
		protected String result;
		protected String question;
		protected String answer;
		protected String advice;
		
		public EsParam(int expId, int id, String corp, String result, String question, String answer, String advice) {
			this.exp_id = expId;
			this.id = id;
			this.corp = corp;
			this.result = result;
			this.question = question;
			this.answer = answer;
			this.advice = advice;
		}
	}
	
	private class ItvParam {
		protected int exp_id;
		protected int id;
		protected String question;
		protected String answer;
		
		public ItvParam(int expId, int id, String question, String answer) {
			this.exp_id = expId;
			this.id = id;
			this.question = question;
			this.answer = answer;
		}
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

	@Override
	public void update(Experience exp) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", exp.getId());
		List<String> oldPos = jdbcTemplate.queryForList("SELECT position FROM senior_positions WHERE exp_id = :id", params, String.class);
		List<String> newPos = exp.getPosition();
		String pos = newPos.stream().collect(Collectors.joining(","));
		String clb = exp.getClub().stream().collect(Collectors.joining(","));
		String ofr = exp.getOffer().stream().collect(Collectors.joining(","));
		params.put("fN", exp.getFamilyName());
		params.put("gN", exp.getGivenName());
		params.put("kFN", exp.getKanaFamilyName());
		params.put("kGN", exp.getKanaGivenName());
		params.put("sex", exp.getSex());
		params.put("mjr", exp.getMajor());
		params.put("uL", exp.getUnivLoc());
		params.put("uN", exp.getUnivName());
		params.put("uF", exp.getUnivFac());
		params.put("uD", exp.getUnivDep());
		params.put("gL", exp.getGradLoc());
		params.put("gN", exp.getGradName());
		params.put("gS", exp.getGradSchool());
		params.put("gD", exp.getGradDiv());
		params.put("gI", exp.getGraduatedIn());
		params.put("deg", exp.getDegree());
		params.put("pos", pos);
		params.put("clb", clb);
		params.put("ofr", ofr);
		String sql = "UPDATE experiences SET "
				+ "family_name = :fN, "
				+ "given_name = :gN, "
				+ "kana_family_name = :kFN, "
				+ "kana_given_name = :kGN, "
				+ "sex = :sex, "
				+ "major = :mjr, "
				+ "univ_loc = :uL, "
				+ "univ_name = :uN, "
				+ "univ_fac = :uF, "
				+ "univ_dep = :uD, "
				+ "grad_loc = :gL, "
				+ "grad_name = :gN, "
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
			params.put("oldPos", oldPos);
			jdbcTemplate.update("DELETE FROM senior_positions WHERE position IN :oldPos AND exp_id = :id", params);
		}
		if (!newPos.isEmpty()) {
			params.clear();
			params.put("newPos", newPos.stream().map(e -> new SenPosParam(exp.getId(), e)).collect(Collectors.toList()));
			jdbcTemplate.update("INSERT INTO senior_positions VALUES :newPos", params);
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
		params.put("id", newItv.getId());
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
		List<String> elements = new LinkedList<String>(Arrays.asList("SELECT COUNT(id) FROM experiences"));
		if (!Objects.equals(target, null) || !target.isEmpty()) elements.addAll(Arrays.asList("WHERE", target.stream().collect(Collectors.joining(" AND "))));
		String sql = elements.stream().collect(Collectors.joining(" "));
		Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
		elements.set(0, "SELECT * FROM experiences");
		if (!Objects.equals(order, null) || !order.isEmpty()) elements.addAll(Arrays.asList("ORDER BY", order.stream().collect(Collectors.joining(", "))));
		sql = elements.stream().collect(Collectors.joining(" "));
		List<Experience> experiences = jdbcTemplate.query(sql, params, (resultSet, i) -> {
				Experience experience = new Experience();
				this.adjustToExp(experience, resultSet);
				return experience;
			});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		result.put("experiences", experiences);
		return result;
	}
}
