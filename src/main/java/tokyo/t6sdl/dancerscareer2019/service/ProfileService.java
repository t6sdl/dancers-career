package tokyo.t6sdl.dancerscareer2019.service;

import org.springframework.stereotype.Service;

import tokyo.t6sdl.dancerscareer2019.model.Profile;
import tokyo.t6sdl.dancerscareer2019.repository.ProfileRepository;

@Service
public class ProfileService {
	private final ProfileRepository profileRepository;
	
	public ProfileService(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}
	
	public void register(Profile newProfile, String loggedInEmail) {
		newProfile.setEmail(loggedInEmail);
		profileRepository.insert(newProfile);
	}
	
	public void delete(String email) {
		profileRepository.delete(email);
	}
	
	public void update(Profile profile, String loggedInEmail) {
		profile.setEmail(loggedInEmail);
		profileRepository.updateAny(profile);
	}
	
	public Profile getProfileByEmail(String email) {
		return profileRepository.findOneByEmail(email);
	}
}
