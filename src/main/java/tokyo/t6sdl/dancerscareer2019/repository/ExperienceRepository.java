package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;
import java.util.Map;

import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;

public interface ExperienceRepository {
	Experience findOneById(int id, boolean all, boolean pv_count);
	Experience findALittleOneById(int id);
	Map<String, Object> find(int sort);
	Map<String, Object> findByName(int sort, String kanaFamilyName, String kanaGivenName);
	Map<String, Object> findByLastName(int sort, String kanaFamilyName);
	Map<String, Object> findByPrefecture(int sort, String univLoc);
	Map<String, Object> findByUniversity(int sort, String univLoc, String univName);
	Map<String, Object> findByFaculty(int sort, String univLoc, String univName, String univFac);
	Map<String, Object> findByDepartment(int sort, String univLoc, String univName, String univFac, String univDep);
	Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch);
	Map<String, Object> findByCreatedAt();
	Es findEsById(int exp_id, int id);
	Interview findInterviewById(int exp_id, int id);
	void insert(Experience newExperience);
	void delete(int id);
	void update(Experience experience);
	void updateLikes(int id, boolean increment);
	void insertEs(Es newEs);
	void deleteEs(int exp_id, int id);
	void updateEs(Es es);
	void insertInterview(Interview newInterview);
	void deleteInterview(int exp_id, int id);
	void updateInterview(Interview interview);
}
