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
	
//	@RequestMapping("/{mentorId}")
//	public String show(@PathVariable("mentorId") Integer mentorId, Model model) {
//		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
//		if (Objects.equals(account, null)) {
//			model.addAttribute("header", "for-stranger");
//		} else if (account.isAdmin()) {
//			model.addAttribute("header", "for-admin");
//		} else {
//			model.addAttribute("header", "for-user");
//		}
//		model.addAttribute("mentorId", mentorId);
//		setMentor(mentorId, model);
//		return "mentors/show";
//	}
	
	private void setMentor(int mentorId, Model model) {
		Map<String, String> mentorInfo = MENTORS.get(mentorId - 1);
		List<String> keys = Arrays.asList("name", "nameEn", "typeNum", "type", "company", "university", "club", "wish");
		keys.forEach(key -> model.addAttribute(key, mentorInfo.get(key)));
	}
	
	private List<Map<String, String>> initializeMentors() {
		List<Map<String, String>> mentors = new ArrayList<Map<String, String>>();
		List<String> names = Arrays.asList("和泉 圭佑", "大島 圭裕", "安西 翔太", "玉津 智基", "宗石 恵多", "今脇 智哉", "伊藤 直樹", "平島 菜那");
		List<String> nameEns = Arrays.asList("Keisuke Izumi", "Keisuke Oshima", "Shota Anzai", "Tomoki Tamatsu", "Keita Muneishi", "Tomoya Imawaki", "Naoki Ito", "Nana Hirashima");
		List<String> typeNums = Arrays.asList("1", "1", "2", "2", "3", "3", "4", "4");
		List<String> types = Arrays.asList("大企業バリバリタイプ", "大企業バリバリタイプ", "ベンチャー上昇志向タイプ", "ベンチャー上昇志向タイプ", "ゆっくり人生楽しみたいタイプ", "ゆっくり人生楽しみたいタイプ", "丁寧に教えてもらいながら仕事を楽しみたいタイプ", "丁寧に教えてもらいながら仕事を楽しみたいタイプ");
		List<String> companies = Arrays.asList("アビームコンサルティング株式会社", "楽天株式会社", "レバレジーズ株式会社", "株式会社出前館", "株式会社揚羽", "ワタベウェディング株式会社/日本交通株式会社/東邦レオ株式会社", "楽天株式会社", "株式会社マイナビ/パーソルテンプスタッフ株式会社");
		List<String> universities = Arrays.asList("明治大学", "慶應義塾大学", "早稲田大学", "法政大学", "立教大学", "早稲田大学", "明治大学", "慶応義塾大学");
		List<String> clubs = Arrays.asList("MDD", "W+I&S", "SesSion", "HSD", "立教大学D-mc", "SesSion", "Zup?", "dance crew es");
		List<String> wishes = Arrays.asList(
				"就活始めたての人や就活結構続けてきたけど最近悩みが多い人などなど、幅広い悩みに答えながらみんなの就活に貢献したいと思ってます！！よろしくー！",
				"内定はしていないのですが、財閥系商社と大手広告系が全て最終面接落ちです。そこで落ちたからこそ学べたこともあるので、相談お待ちしてます！",
				"選択肢はいくらでもあります！周りは気にせず、あなたが1番納得する就活ができるように全力を尽くします！",
				"就活って正直全然わからないと思います。僕もそうでした！やはり、1番効率がいいのは\"知ってる人から聞く\"ことです！なので1人で就活を先延ばしにしない為にもどんどん聞いてください！全力でサポートします！",
				"自分は就活開始が一般的な大学生よりも遅かったです。遅くても諦める必要はないけど、早ければ早いほど自分を知った就活ができると思います！一緒に頑張っていきましょう！",
				"就活始めたての人もこれからの人もまだ全然わからない人も、一生懸命サポートさせていただきます！よろしくお願いします〜",
				"就職はバリバリの技術職ですが就活初期は企画職や広告系といったいわゆる文系職なども考えてました！なので「まず何したらいいの？」から「技術職って実際どうなの？」まで多くの人の相談に乗ることができます！よろしくです！",
				"お疲れ様です！es4年のななぴです！就活でいっちばん大事なのは自己分析だと思ってます！わたしもたくさんの先輩やOBOGに自己分析を手伝ってもらって就活乗り越えることができました。一緒に自己分析していきましょう、お手伝いさせてください！"
				);
		for (int i = 0; i < 8; i++) {
			Map<String, String> mentorInfo = new HashMap<String, String>();
			mentorInfo.put("name", names.get(i));
			mentorInfo.put("nameEn", nameEns.get(i));
			mentorInfo.put("typeNum", typeNums.get(i));
			mentorInfo.put("type", types.get(i));
			mentorInfo.put("company", companies.get(i));
			mentorInfo.put("university", universities.get(i));
			mentorInfo.put("club", clubs.get(i));
			mentorInfo.put("wish", wishes.get(i));
			mentors.add(mentorInfo);
		}
		return mentors;
	}
}
