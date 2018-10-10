package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;
import java.util.Map;

import tokyo.t6sdl.dancerscareer2019.model.Profile;

public interface ProfileRepository {
	Profile findOneByEmail(String email);
	Map<String, Object> find();
	Map<String, Object> findByName(String kanaLastName, String kanaFirstName);
	Map<String, Object> findByLastName(String kanaLastName);
	Map<String, Object> findByPrefecture(String prefecture);
	Map<String, Object> findByUniversity(String prefecture, String university);
	Map<String, Object> findByFaculty(String prefecture, String university, String faculty);
	Map<String, Object> findByDepartment(String prefecture, String university, String faculty, String department);
	Map<String, Object> findByPosition(List<String> position, boolean andSearch);
	String findLastNameByEmail(String email);
	List<String> findLikesByEmail(String email);
	void insert(Profile newProfile);
	void delete(String email);
	void updateAny(Profile profile);
	void updateLikes(String email, List<String> likes);
}
