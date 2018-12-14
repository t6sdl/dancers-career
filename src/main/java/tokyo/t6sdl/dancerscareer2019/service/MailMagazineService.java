package tokyo.t6sdl.dancerscareer2019.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.io.EmailSender;
import tokyo.t6sdl.dancerscareer2019.model.Experience;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;
import tokyo.t6sdl.dancerscareer2019.repository.ExperienceRepository;

@RequiredArgsConstructor
@Service
public class MailMagazineService {
	private final ExperienceRepository experienceRepository;
	private final AccountRepository accountRepository;
	private final EmailSender emailSender;
	
	@Scheduled(cron="0 0 18 * * 5", zone="Asia/Tokyo")
	public void sendNewEsMail() {
		List<String> emails = accountRepository.findEmailByNewEsMail();
		if (Objects.equals(emails, null) || emails.isEmpty()) return;
		Map<String, Object> results = experienceRepository.findByCreatedAt();
		if (Objects.equals(results, null) || (Integer) results.get("count") == 0) return;
		Mail mail = new Mail(emails, Mail.SUB_NEW_ES);
		@SuppressWarnings("unchecked")
		List<Experience> experiences = (List<Experience>) results.get("experiences");
		mail.setUrl(Mail.URI_EXPERIENCES + "/" + experiences.get(0).getExperience_id());
		emailSender.sendMailMagazine(mail);
	}
}
