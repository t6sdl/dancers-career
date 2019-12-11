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
	Map<String, Object> findByFamilyName(int sort, String kanaFamilyName);
	Map<String, Object> findByUnivLoc(int sort, String univLoc);
	Map<String, Object> findByUnivName(int sort, String univLoc, String univName);
	Map<String, Object> findByUnivFac(int sort, String univLoc, String univName, String univFac);
	Map<String, Object> findByUnivDep(int sort, String univLoc, String univName, String univFac, String univDep);
	Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch);
	Map<String, Object> findByCreatedAt();
	Es findEsById(int expId, int id);
	Interview findInterviewById(int expId, int id);
	void insert(Experience newExperience);
	void delete(int id);
	void update(Experience experience);
	void updateLikes(int id, boolean increment);
	void insertEs(Es newEs);
	void deleteEs(int expId, int id);
	void updateEs(Es es);
	void insertInterview(Interview newInterview);
	void deleteInterview(int expId, int id);
	void updateInterview(Interview interview);
}
