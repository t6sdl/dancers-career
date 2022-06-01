package tokyo.t6sdl.dancerscareer.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.io.EmailSender;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.model.Experience;
import tokyo.t6sdl.dancerscareer.model.Mail;
import tokyo.t6sdl.dancerscareer.repository.AccountRepository;
import tokyo.t6sdl.dancerscareer.repository.ExperienceRepository;

@RequiredArgsConstructor
@Service
public class MailMagazineService {
	private final ExperienceRepository experienceRepository;
	private final AccountRepository accountRepository;
	private final EmailSender emailSender;

	@Scheduled(cron="0 0 18 * * 5", zone="Asia/Tokyo")
	public void announceNewEs() {
		if (System.getProperty("env", "development").equals("staging")) return;
		List<Account> accounts = accountRepository.findForMassMailBy(1);
		if (Objects.equals(accounts, null) || accounts.isEmpty()) return;
		Map<String, Object> results = experienceRepository.findByCreatedAt();
		if (Objects.equals(results, null) || (Integer) results.get("count") == 0) return;
		Mail mail = new Mail(accounts, Mail.SUB_NEW_ES);
		@SuppressWarnings("unchecked")
		List<Experience> experiences = (List<Experience>) results.get("experiences");
		mail.setExperiences(experiences);
		emailSender.sendMassMail(mail);
	}

//	@Scheduled(cron="0 0 18 28 12 5", zone="Asia/Tokyo")
//	public void surveyUserFriendly() {
//		List<Account> accounts = accountRepository.findForMassMailBy(0);
//		if (Objects.equals(accounts, null) || accounts.isEmpty()) return;
//		Mail mail = new Mail(accounts, Mail.SUB_SURVEY);
//		emailSender.sendMassMail(mail);
//	}
}
