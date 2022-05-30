package tokyo.t6sdl.dancerscareer.repository;

import java.util.List;

import tokyo.t6sdl.dancerscareer.model.Mentor;

public interface MentorRepository {
	List<Mentor> getAll();
	Mentor find(int id);
	void create(Mentor mentor);
	void update(Mentor mentor);
	void destroy(int id);
}
