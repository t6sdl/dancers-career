package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;
import java.util.Map;

import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;

public interface ExperienceRepository {
	Experience findOneById(int experience_id, boolean all, boolean pv_count);
	Map<String, Object> find();
	Map<String, Object> findByName(String kanaLastName, String kanaFirstName);
	Map<String, Object> findByLastName(String kanaLastName);
	Map<String, Object> findByPrefecture(String prefecture);
	Map<String, Object> findByUniversity(String prefecture, String university);
	Map<String, Object> findByFaculty(String prefecture, String university, String faculty);
	Map<String, Object> findByDepartment(String prefecture, String university, String faculty, String department);
	Map<String, Object> findByPosition(List<String> position, boolean andSearch);
	Es findEsById(int experience_id, int es_id);
	Interview findInterviewById(int experience_id, int interview_id);
	void insert(Experience newExperience);
	void delete(int experience_id);
	void update(Experience experience);
	void updateLikes(int experience_id, boolean increment);
	void insertEs(Es newEs);
	void deleteEs(int experience_id, int es_id);
	void updateEs(Es es);
	void insertInterview(Interview newInterview);
	void deleteInterview(int experience_id, int interview_id);
	void updateInterview(Interview interview);
}
