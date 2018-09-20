package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;

import tokyo.t6sdl.dancerscareer2019.model.Es;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Interview;

public interface ExperienceRepository {
	List<Experience> find();
	List<Experience> findByName(String kanaLastName, String kanaFirstName);
	List<Experience> findByLastName(String kanaLastName);
	List<Experience> findByPrefecture(String prefecture);
	List<Experience> findByUniversity(String prefecture, String university);
	List<Experience> findByFaculty(String prefecture, String university, String faculty);
	List<Experience> findByDepartment(String prefecture, String university, String faculty, String department);
	List<Experience> findByPosition(List<String> position, String method);
	void insert(Experience newExperience);
	void delete(int experience_id);
	void update(Experience experience);
	void insertEs(Es newEs);
	void deleteEs(int experience_id, int es_id);
	void updateEs(Es es);
	void insertInterview(Interview newInterview);
	void deleteInterview(int experience_id, int interview_id);
	void updateInterview(Interview interview);
}
