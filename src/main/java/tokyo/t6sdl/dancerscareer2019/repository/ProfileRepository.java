package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;

import tokyo.t6sdl.dancerscareer2019.model.Profile;

public interface ProfileRepository {
	List<Profile> find();
	Profile findOneByEmail(String email);
	List<Profile> findByName(String kanaLastName, String kanaFirstName);
	List<Profile> findByPrefecture(String prefecture);
	List<Profile> findByUniversity(String university);
	List<Profile> findByFaculty(String university, String faculty);
	List<Profile> findByDepartment(String university, String faculty, String department);
	List<Profile> findByPosition(List<String> positions);
	void insert(Profile newProfile);
	void delete(String email);
	void updateAny(Profile profile);
}
