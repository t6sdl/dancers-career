package tokyo.t6sdl.dancerscareer2019.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.io.EmailSender;
import tokyo.t6sdl.dancerscareer2019.model.Mail;
import tokyo.t6sdl.dancerscareer2019.repository.AccountRepository;

@RequiredArgsConstructor
@Service
public class MailMagazineService {
	private final AccountRepository accountRepository;
	private final EmailSender emailSender;
	
	@Scheduled(cron="0 55 * * * *", zone="Asia/Tokyo")
	public void sendNewEsMail() {
		List<String> emails = accountRepository.findEmailByNewEsMail();
		Mail mail = new Mail(emails, Mail.SUB_NEW_ES);
		emailSender.sendMailMagazine(mail);
	}
}
