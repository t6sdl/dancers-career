package tokyo.t6sdl.dancerscareer2019.repository;

import java.util.List;

import tokyo.t6sdl.dancerscareer2019.model.Mentor;

public interface MentorRepository {
	List<Mentor> getAll();
	Mentor find(int id);
	void create(Mentor mentor);
	void update(Mentor mentor);
	void destroy(int id);
}
