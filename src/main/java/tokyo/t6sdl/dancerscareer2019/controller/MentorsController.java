package tokyo.t6sdl.dancerscareer2019.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mentors")
public class MentorsController {
	private final SecurityService securityService;
	private final AccountService accountService;
	private final List<Map<String, String>> MENTORS = initializeMentors();

	@RequestMapping()
	public String index(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		return "mentors/index";		
	}
	
	@RequestMapping("/{mentorId}")
	public String show(@PathVariable("mentorId") Integer mentorId, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		model.addAttribute("mentorId", mentorId);
		setMentor(mentorId, model);
		return "mentors/show";
	}
	
	private void setMentor(int mentorId, Model model) {
		Map<String, String> mentorInfo = MENTORS.get(mentorId - 1);
		List<String> keys = Arrays.asList("name", "nameEn", "typeNum", "type", "company", "university", "club", "action", "wish");
		keys.forEach(key -> model.addAttribute(key, mentorInfo.get(key)));
	}
	
	private List<Map<String, String>> initializeMentors() {
		List<Map<String, String>> mentors = new ArrayList<Map<String, String>>();
		List<String> names = Arrays.asList("濱崎 優希奈", "牟田口 真輝", "阿部野 紗弥", "九鬼 嘉隆", "洲崎 汐音", "田沢 尚生", "大森 萌子", "髙島 左京", "大澤 奈々子", "岡崎 仁美");
		List<String> nameEns = Arrays.asList("Yukina Hamasaki", "Masaki Mutaguchi", "Saya Abeno", "Yoshitaka Kuki", "Shione Suzaki", "Nao Tazawa", "Moeko Omori", "Sakyo Takashima", "Nanako Osawa", "Hitomi Okazaki");
		List<String> typeNums = Arrays.asList("1", "1", "2", "2", "2", "2", "3", "4", "4", "4");
		List<String> types = Arrays.asList("大企業バリバリタイプ", "大企業バリバリタイプ", "ベンチャー上昇志向タイプ", "ベンチャー上昇志向タイプ", "ベンチャー上昇志向タイプ", "ベンチャー上昇志向タイプ", "ゆっくり人生楽しみたいタイプ", "丁寧に教えてもらいながら仕事を楽しみたいタイプ", "丁寧に教えてもらいながら仕事を楽しみたいタイプ", "丁寧に教えてもらいながら仕事を楽しみたいタイプ");
		List<String> companies = Arrays.asList("三井不動産レジデンシャル株式会社/株式会社ゴールドクレスト/株式会社肥後銀行", "未定", "株式会社Sansan", "未定", "株式会社マイクロアド", "株式会社マイクロアド", "株式会社パソナグループ/株式会社ニチイ学館", "株式会社マイナビ", "株式会社EMシステムズ", "三井不動産リアルティ株式会社/三井不動産リフォーム株式会社/JKホールディングス株式会社");
		List<String> universities = Arrays.asList("中央大学", "横浜国立大学", "明治大学", "慶應義塾大学", "上智大学", "東北大学大学院", "立教大学", "東北学院大学", "立教大学", "立教大学");
		List<String> clubs = Arrays.asList("NAOKAN", "R3ude", "立教大学D-mc", "Dancing Crew JADE", "Gsplash", "ストリートダンスサークルWHO", "立教大学D-mc", "Dance Factory's", "立教大学D-mc", "立教大学D-mc");
		List<String> actions = Arrays.asList(
				"6月から開始し、7月から12月はまたダンスに打ち込んだ後、また就活を再開しました！ダンキャリでガクチカを一緒に練ってもらいました！",
				"1月から就活開始。特に業界は定めず幅広く見ていた。2月頃にダンキャリと出会い、自己分析・ガクチカ等をブラッシュアップしてもらう。それ以来、IT・広告業界に絞り就活を進める。",
				"12月の公演まではダンス漬けの毎日でした！ダンキャリでの自己分析を中心に就活を進めた結果、「モチベーション高く働ける会社」に出会うことができ、内定をもらうことができました！",
				"学園祭が終わって、11月くらいから就活を始めた。ダンキャリで面談をしてもらい、就活の流れや本質を教えてもらったおかげで、効率よく進めることができた。様々な社会人と話すことを楽しみながら就活していた。",
				"3年の4月から就活に向けて少しずつ動き始めました。しかし、ダンスとの両立がうまくいかず、就活のモチベーションは上がったり下がったりのジェットコースター状態、、、。 そんなときダンサーズキャリアの面談を利用して、就活についての本質的な考え方を学びました！その中の１つは、自己分析をしっかりやることでした。自分の経験や感情を言語化をすることで、面接で伝えたいことをしっかり話せるようになり。無事第一志望の企業で内定獲得しました。",
				"関東の友人がきっかけでM1の6月頃にコンサルタントという職業に興味が湧く。しかししっくりこない中、ダンキャリの面談を通して、ダンスの経験なども踏まえて今までのありのままの自分でやりたい事を頑張れる環境を考えられるようになる。そうして自分が社会人でこうしたい、こうありたいを叶えられると希望をもてる内定を得ることが出来た。",
				"6月から本格的に開始しましたがサークル、部活動のコーチ、教職課程も並行させこれらの合間にインターンや企業説明会に参加していました。最も自己分析に力を入れていたのでダンキャリの方々にも大変お世話になりました。",
				"ダンスばかりの学生生活を送っていたので、就職活動を始めたのは、4年生の4月からでした。その時点でみんなよりかはスタートが遅れていましたが、ダンキャリで、自己分析、面談等をしてもるった結果、6月に第1志望の会社の内定を頂くことができました！",
				"3年夏~秋：様々な業界の1dayインターン参加<br>10月：自己分析開始<br>12月：引退してから就活を本格化、業界研究など<br>1月~3月解禁後：SPI対策、自己分析から自分の就活軸を定めそれに合う企業選、会社説明会(オンラインがほとんど)に参加、ES提出、模擬面接<br>5月下旬：内定",
				"3年の7月くらいから就活を始めましたが、サークルの公演があり、12月までダンスばかりでした。どんな活動も自己分析に繋がると思って、短いインターンには20社くらい行きました。その中から選考に進むところを決めたりして、6月に今の内定先に就職することを決めました。"
				);
		List<String> wishes = Arrays.asList(
				"ダンサーが自信持って就活に挑むことができるようにサポートしていきます！",
				"まだ終わっておりませんが、だからこそ長期化してしまった就活の進め方などをお伝えしたいと考えております。皆さんの支えになれますように頑張ります！",
				"自分について知ることが最大の鍵になってくると思います！ダンスという共通の環境に居たからこそ、理解しあえる事があると思います。あなたの1番の味方がここには沢山いるので、是非面談しに来てくださいー！",
				"人と話すのが大好きです！あなたの強みや弱み、理想の働き方を、一緒に探っていきましょう！優しさと面倒見の良さでは、僕の右に出る者はいません！！",
				"ダンスをやっている中で、楽しい経験だけでなく沢山辛い経験をしてきたと思います。その経験は、きっとあなたの強みで乗り越えたはず。その強みを就職活動でしっかり伝えられるようになりましょう！私は、そのお手伝いを精一杯させていただければと思ってます！サークルでは母キャラだったので、頼られれば頼られるほど頑張っちゃいます！笑　気軽になんでも相談してください！面談お待ちしております。",
				"人生の中でも就職活動というのは無限の選択肢がありすぎて正直よく分かんなくなる事があると思いますが、一旦この就活を通して、自分と向き合い自分をよく理解して、納得のいく形で就活して頂けるように頑張りたいです。",
				"ダンスを頑張ってきた方、気付いたら何気なくでもずっとダンス生活を送っていた方まで皆さんの経験や良い所を多く引き出し言語化できるよう私自身も一緒に試行錯誤していきたいと思っています！気軽な気持ちで飛び込んでくれたら嬉しいです！",
				"自分は超がつくほど、ダンスが大好きなので、まずはダンスのお話などから、気軽に話していきたいなと思ってます！その中で、みんながダンスを頑張ったエピソードが、就職活動に活きるようにする術を、一緒に見つけていきたいと思っています！",
				"ダンスを通じた経験すべてに自信と誇りをもって！ その経験をただの思い出から、「今の自分を形成したプロセス」と昇格させ、みなさんのこれからに活かしていけるよう、丁寧に手伝わせていただきます！",
				"はじめまして！岡崎仁美です！私がサークルと就活を同時並行で出来るか悩んだ時、ダンスを頑張ることが就活になると言ってくれた人がいました。どんな形であれ、ダンスが好きでサークルを続けたことは、胸を張って言えることだし、どんなことも自分の経験になり、自分らしさを発見するチャンスになると私は思っています。ゆっくりお話しながら、一緒に頑張っていきましょう！"
				);
		for (int i = 0; i < names.size(); i++) {
			Map<String, String> mentorInfo = new HashMap<String, String>();
			mentorInfo.put("name", names.get(i));
			mentorInfo.put("nameEn", nameEns.get(i));
			mentorInfo.put("typeNum", typeNums.get(i));
			mentorInfo.put("type", types.get(i));
			mentorInfo.put("company", companies.get(i));
			mentorInfo.put("university", universities.get(i));
			mentorInfo.put("club", clubs.get(i));
			mentorInfo.put("action", actions.get(i));
			mentorInfo.put("wish", wishes.get(i));
			mentors.add(mentorInfo);
		}
		return mentors;
	}
}
