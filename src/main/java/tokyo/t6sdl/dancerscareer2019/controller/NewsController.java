package tokyo.t6sdl.dancerscareer2019.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
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
import tokyo.t6sdl.dancerscareer2019.httpresponse.NotFound404;
import tokyo.t6sdl.dancerscareer2019.model.Account;
import tokyo.t6sdl.dancerscareer2019.service.AccountService;
import tokyo.t6sdl.dancerscareer2019.service.SecurityService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/news")
public class NewsController {
	private final SecurityService securityService;
	private final AccountService accountService;
	private final List<Map<String, Object>> newsList = generateNewsList();
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
	
	private List<Map<String, Object>> generateNewsList() {
		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		List<Integer> ids = Arrays.asList(1, 21, 31, 41, 51, 61, 71);
		List<String> shortTitles = Arrays.asList(
				"ダンサー向け就活セミナーが開催されます！",
				"【19卒ダンキャリ利用者インタビュー第1弾】〜就活ダルいと言っていた僕が今、仕事を楽しんでいる理由〜",
				"【19卒ダンキャリ利用者インタビュー第2弾】〜化粧品メーカーはただの憧れでしかなかった〜",
				"【19卒ダンキャリ利用者インタビュー第3弾】〜何にもわからない状態からベストマッチな会社へ〜",
				"「ダンスの良さを伝えたら大手メーカーの面接落ちた」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜前編〜",
				"「就活を終えた今だから思うダンサー人材の売込み方」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜後編〜",
				"ダンスの経験で超難関企業リクルートに内定！IT新規事業、外資就活ドットコムの経営まで手がけるビジネスマンは元ダンサーだった！"
			);
		List<String> titles = Arrays.asList(
				"ダンサー向け就活セミナーが開催されます！",
				"【19卒ダンキャリ利用者インタビュー第1弾】〜就活ダルいと言っていた僕が今、仕事を楽しんでいる理由〜",
				"【19卒ダンキャリ利用者インタビュー第2弾】〜化粧品メーカーはただの憧れでしかなかった〜 「好きを仕事にすることは違う」に気づけた本質的な就活の思考法。",
				"【19卒ダンキャリ利用者インタビュー第3弾】〜何にもわからない状態からベストマッチな会社へ〜",
				"「ダンスの良さを伝えたら大手メーカーの面接落ちた」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜前編〜",
				"「就活を終えた今だから思うダンサー人材の売込み方」日本最大規模のダンスサークル代表とジャンルリーダーが語る偽りのない就活談〜後編〜",
				"ダンスの経験で超難関企業リクルートに内定！飲食店・美容室向けIT新規事業、外資就活ドットコムの経営まで手がけるビジネスマンは元ダンサーだった！"
			);
		List<LocalDate> dates = Arrays.asList(LocalDate.of(2018, 12, 6), LocalDate.of(2019, 12, 21), LocalDate.of(2019, 12, 28), LocalDate.of(2020, 1, 4), LocalDate.of(2020, 2, 15), LocalDate.of(2020, 2, 22), LocalDate.of(2020, 7, 19));
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> news = new HashMap<String, Object>();
			news.put("id", ids.get(i));
			news.put("title", titles.get(i));
			news.put("shortTitle", shortTitles.get(i));
			news.put("updatedAt", dates.get(i));
			newsList.add(news);
		}
		Map<Integer, Map<String, Object>> newsMap = new HashMap<Integer, Map<String, Object>>();
		newsList.forEach((n) -> { newsMap.put((Integer) n.get("id"), n); });
		return newsList.stream().sorted((n1, n2) -> {
			LocalDate updatedAt1 = (LocalDate) n1.get("updatedAt");
			LocalDate updatedAt2 = (LocalDate) n2.get("updatedAt");
			int ret = updatedAt2.compareTo(updatedAt1);
			return ret == 0 ? -1 : ret;
		}).collect(Collectors.toList());
	}
	
	private Map<Integer, Map<String, Object>> generateNewsMap() {
		Map<Integer, Map<String, Object>> newsMap = new HashMap<Integer, Map<String, Object>>();
		newsList.forEach((n) -> { newsMap.put((Integer) n.get("id"), n); });
		return newsMap;
	}
}
