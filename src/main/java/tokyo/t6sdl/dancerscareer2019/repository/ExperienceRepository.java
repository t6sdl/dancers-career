package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;
import java.util.Map;

import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;

public interface ExperienceRepository {
	Experience findOneById(int experience_id, boolean all, boolean pv_count);
	Experience findOneByIdForStranger(int experience_id);
	Map<String, Object> find(int sort);
	Map<String, Object> findByName(int sort, String kanaLastName, String kanaFirstName);
	Map<String, Object> findByLastName(int sort, String kanaLastName);
	Map<String, Object> findByPrefecture(int sort, String univPref);
	Map<String, Object> findByUniversity(int sort, String univPref, String univName);
	Map<String, Object> findByFaculty(int sort, String univPref, String univName, String faculty);
	Map<String, Object> findByDepartment(int sort, String univPref, String univName, String faculty, String department);
	Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch);
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
