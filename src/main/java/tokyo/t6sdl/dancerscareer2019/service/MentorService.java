package tokyo.t6sdl.dancerscareer2019.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Mentor;
import tokyo.t6sdl.dancerscareer2019.repository.MentorRepository;

@RequiredArgsConstructor
@Service
public class MentorService {
	private final MentorRepository mentorRepository;
	
	public Mentor find(int id) {
		return mentorRepository.find(id);
	}
	
	public List<Mentor> getAll() {
		return mentorRepository.getAll();
	}
	
	public void create(Mentor mentor) {
		mentorRepository.create(mentor);
	}
	
	public void destroy(int id) {
		mentorRepository.destroy(id);
	}
}
