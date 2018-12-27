package tokyo.t6sdl.dancerscareer2019.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.io.EmailSender;
import tokyo.t6sdl.dancerscareer2019.model.Account;
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
	
	@Scheduled(cron="0 10 18 * * 5", zone="Asia/Tokyo")
	public void announceNewEs() {
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
	
	@Scheduled(cron="0 0 18 28 12 5", zone="Asia/Tokyo")
	public void surveyUserFriendly() {
		List<Account> accounts = accountRepository.findForMassMailBy(0);
		if (Objects.equals(accounts, null) || accounts.isEmpty()) return;
		Mail mail = new Mail(accounts, Mail.SUB_SURVEY);
		emailSender.sendMassMail(mail);
	}
	
	@Scheduled(cron="0 0 8 28 12 5", zone="Asia/Tokyo")
	public void apologize() {
		List<Account> accounts = accountRepository.findForMassMailBy(0);
		if (Objects.equals(accounts, null) || accounts.isEmpty()) return;
		Mail mail = new Mail(accounts, "深夜に発生したメール誤送信のお詫び");
		mail.setContent("ダンサーズキャリアをご利用の皆様へ\n\n【深夜に発生したメール誤送信のお詫び】\n\n本日午前3時35分ごろ、アンケートへのご協力をお願いする旨のメールが誤って皆様に送信される事態が発生いたしました。\n本来は本日午後6時ごろに配信される予定のメールであり、深夜という非常識な時間帯に誤って送信されてしまったことを深くお詫び申し上げます。誠に申し訳ございませんでした。\n今後はこのような事態が起きぬよう細心の注意を払って管理を徹底して参ります。\n\nなお、アンケートのリンクは間違っており、本日午後6時ごろに正しいメールを再度ご案内いたしますので、何卒アンケートにご協力くださいますようよろしくお願い申し上げます。\n\n今後ともダンサーズキャリアをよろしくお願いいたします。\n\n\nマイページ\nhttps://dancers-career.t6sdl.tokyo/user\n\nお問い合わせ\nhttps://dancers-career.t6sdl.tokyo/about/contact");
		emailSender.sendMassTextMail(mail);
	}
}
