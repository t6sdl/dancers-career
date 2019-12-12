package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;
import java.util.Map;

import tokyo.t6sdl.dancerscareer2019.model.Profile;

public interface ProfileRepository {
	Profile findOneByEmail(String email);
	Map<String, Object> find(int sort);
	Map<String, Object> findByName(int sort, String kanaFamilyName, String kanaGivenName);
	Map<String, Object> findByFamilyName(int sort, String kanaFamilyName);
	Map<String, Object> findByUnivLoc(int sort, String univLoc);
	Map<String, Object> findByUnivName(int sort, String univLoc, String univName);
	Map<String, Object> findByUnivFac(int sort, String univLoc, String univName, String univFac);
	Map<String, Object> findByUnivDep(int sort, String univLoc, String univName, String univFac, String univDep);
	Map<String, Object> findByPosition(int sort, List<String> position, boolean andSearch);
	String findFamilyNameByEmail(String email);
	List<String> findLikesByEmail(String email);
	void insert(Profile newProfile);
	void delete(String email);
	void update(Profile profile);
	void updateLikes(String email, List<String> likes);
}
