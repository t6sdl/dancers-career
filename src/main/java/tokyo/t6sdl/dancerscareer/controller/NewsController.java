package tokyo.t6sdl.dancerscareer.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer.model.Account;
import tokyo.t6sdl.dancerscareer.service.AccountService;
import tokyo.t6sdl.dancerscareer.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/news")
public class NewsController {
	private final SecurityService securityService;
	private final AccountService accountService;
	// NOTE: 10刻みの数値
	private final List<Integer> newsIds = Arrays.asList(1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 111, 121, 131, 141, 151, 161, 171, 181, 191, 201);
	// NOTE: 記事一覧ページで表示する短めの記事タイトル。65文字まで。
	private final List<String> newsShortTitles = Arrays.asList(
			"ダンサー向け就活セミナーが開催されます！",
			"【19卒ダンキャリ利用者インタビュー第1弾】〜就活ダルいと言っていた僕が今、仕事を楽しんでいる理由〜",
			"【19卒ダンキャリ利用者インタビュー第2弾】〜化粧品メーカーはただの憧れでしかなかった〜",
			"【19卒ダンキャリ利用者インタビュー第3弾】〜何にもわからない状態からベストマッチな会社へ〜",
			"「ダンスの良さを伝えたら大手メーカーの面接落ちた」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜前編〜",
			"「就活を終えた今だから思うダンサー人材の売込み方」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜後編〜",
			"ダンスの経験で超難関企業リクルートに内定！IT新規事業、外資就活ドットコムの経営まで手がけるビジネスマンは元ダンサーだった！",
			"【21卒ダンキャリ利用者インタビュー第1弾】〜キャリア面談を通して身につけた自分の見つめ方、イメージと現実の違いとは〜",
			"【21卒ダンキャリ利用者インタビュー第2弾】”代表”だけでは通過しない！人事がダンスの話で注目するポイントとは！？",
			"これで就活は怖くない！〜ダンキャリ活用アドバイス〜",
			"23卒 就活無双×就活苦労 〜同じサークル、同じ役職、同じ志望業界 この二人の間あった”圧倒的差”から見出す”就活の近道”〜",
			"【24卒向けセミナー vol.1】 ダンスサークル出身社会人から学ぶ納得内定への最短ルート",
			"【就活バイブル 第1弾】就活とは何なのか 〜”何故就活をするのか”あなたは説明できますか！？〜",
			"【就活バイブル 第2弾】就活のゴールとは 〜”ワクワクする将来”を考えたことがありますか！？〜",
			"【就活バイブル 第3弾】就活の流れとは 〜何からやればいいの？サークルを引退してからでも遅くない！？〜",
			"【就活バイブル 第4弾】就活でやることとは 〜やることは3つだけ！シンプルに考えよう！〜",
			"【就活バイブル 第5弾】自己分析 〜強み弱みだけじゃ分からない...自分の”何を”知れれば良いの？〜",
			"先輩ダンサーに聞く！就活成功プロセス ~ダンサー就活タイプ診断~",
			"先輩ダンサーに聞く！就活成功プロセス ~引退後の進め方編~ G-splash M.Kさん",
			"【就活バイブル 第6弾】企業研究 前提編 〜福利厚生や初任給で判断するな！知るべきポイントとは！？〜"
		);
	// NOTE: 記事最上部に表示する正式な記事タイトル。文字数制限なし。
	private final List<String> newsTitles = Arrays.asList(
			"ダンサー向け就活セミナーが開催されます！",
			"【19卒ダンキャリ利用者インタビュー第1弾】〜就活ダルいと言っていた僕が今、仕事を楽しんでいる理由〜",
			"【19卒ダンキャリ利用者インタビュー第2弾】〜化粧品メーカーはただの憧れでしかなかった〜 「好きを仕事にすることは違う」に気づけた本質的な就活の思考法。",
			"【19卒ダンキャリ利用者インタビュー第3弾】〜何にもわからない状態からベストマッチな会社へ〜",
			"「ダンスの良さを伝えたら大手メーカーの面接落ちた」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜前編〜",
			"「就活を終えた今だから思うダンサー人材の売込み方」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜後編〜",
			"ダンスの経験で超難関企業リクルートに内定！飲食店・美容室向けIT新規事業、外資就活ドットコムの経営まで手がけるビジネスマンは元ダンサーだった！",
			"【21卒ダンキャリ利用者インタビュー第1弾】〜キャリア面談を通して身につけた自分の見つめ方、イメージと現実の違いとは〜",
			"【21卒ダンキャリ利用者インタビュー第2弾】”代表”だけでは通過しない！人事がダンスの話で注目するポイントとは！？",
			"これで就活は怖くない！〜ダンキャリ活用アドバイス〜",
			"23卒 就活無双×就活苦労 〜同じサークル、同じ役職、同じ志望業界 この二人の間あった”圧倒的差”から見出す”就活の近道”〜",
			"【24卒向けセミナー vol.1】 ダンスサークル出身社会人から学ぶ納得内定への最短ルート",
			"【就活バイブル 第1弾】就活とは何なのか 〜”何故就活をするのか”あなたは説明できますか！？〜",
			"【就活バイブル 第2弾】就活のゴールとは 〜”ワクワクする将来”を考えたことがありますか！？〜",
			"【就活バイブル 第3弾】就活の流れとは 〜何からやればいいの？サークルを引退してからでも遅くない！？",
			"【就活バイブル 第4弾】就活でやることとは 〜やることは3つだけ！シンプルに考えよう！〜",
			"【就活バイブル 第5弾】自己分析 〜強み弱みだけじゃ分からない...自分の”何を”知れれば良いの？〜",
			"先輩ダンサーに聞く！就活成功プロセス ~ダンサー就活タイプ診断~",
			"先輩ダンサーに聞く！就活成功プロセス ~引退後の進め方編~ G-splash M.Kさん",
			"【就活バイブル 第6弾】企業研究 前提編 〜福利厚生や初任給で判断するな！知るべきポイントとは！？〜"
		);
	// NOTE: 記事アップロード日。
	private final List<String> newsUpdatedDates = Arrays.asList(
			"2018-12-06",
			"2019-12-21",
			"2019-12-28",
			"2020-01-04",
			"2020-02-15",
			"2020-02-22",
			"2020-07-19",
			"2021-05-25",
			"2021-09-20",
			"2022-06-01",
			"2022-07-15",
			"2022-08-01",
			"2022-08-15",
			"2022-09-01",
			"2022-10-10",
			"2022-11-01",
			"2022-11-15",
			"2022-12-01",
			"2022-12-01",
			"2022-12-26"
		);
	private final Map<Integer, Map<String, Object>> newsMap = generateNewsMap();

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
		List<Map<String, Object>> newsList = newsMap.values().stream().sorted((n1, n2) -> {
			LocalDate updatedAt1 = (LocalDate) n1.get("updatedAt");
			LocalDate updatedAt2 = (LocalDate) n2.get("updatedAt");
			int ret = updatedAt2.compareTo(updatedAt1);
			return ret == 0 ? -1 : ret;
		}).collect(Collectors.toList());
		model.addAttribute("newsList", newsList);
		return "news/index";
	}

	@RequestMapping("/{newsId}")
	public String show(@PathVariable("newsId") Integer newsId, Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
		MutableDataSet options = new MutableDataSet();
		options.set(Parser.EXTENSIONS,
				Arrays.asList(
						AttributesExtension.create(),
						AutolinkExtension.create(),
						EmojiExtension.create(),
						FootnoteExtension.create(),
						InsExtension.create(),
						StrikethroughExtension.create(),
						TablesExtension.create(),
						TocExtension.create()
				));
		options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();
		Path path = Paths.get("src/main/resources/static/md/" + newsId + ".md");
		List<String> lines;
		try {
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NotFound404();
		}
		Node document = parser.parse(lines.stream().collect(Collectors.joining("\n")));
		String html = renderer.render(document);
		model.addAttribute("md", html);
		model.addAttribute("id", newsId);
		model.addAttribute("title", newsMap.get(newsId).get("title"));
		model.addAttribute("updatedAt", newsMap.get(newsId).get("updatedAt"));
		return "news/show";
	}

	private Map<Integer, Map<String, Object>> generateNewsMap() {
		Map<Integer, Map<String, Object>> newsMap = new HashMap<Integer, Map<String, Object>>();
		for (int i = 0; i < newsIds.size(); i++) {
			Map<String, Object> news = new HashMap<String, Object>();
			news.put("id", newsIds.get(i));
			news.put("title", newsTitles.get(i));
			news.put("shortTitle", newsShortTitles.get(i));
			news.put("updatedAt", LocalDate.parse(newsUpdatedDates.get(i)));
			newsMap.put(newsIds.get(i), news);
		}
		return newsMap;
	}
}
