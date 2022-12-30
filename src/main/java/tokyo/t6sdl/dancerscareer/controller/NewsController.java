package tokyo.t6sdl.dancerscareer.controller;

import java.io.IOException;
import java.io.InputStream;
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

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yaml.snakeyaml.Yaml;

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
	private final List<Map<String, Object>> newsMetadataList = generateNewsMetadataList();

	@RequestMapping()
	public String index(Model model) {
		setHeader(model);
		model.addAttribute("newsList", newsMetadataList);
		return "news/index";
	}

	@RequestMapping("/{newsId}")
	public String show(@PathVariable("newsId") Integer newsId, Model model) {
		setHeader(model);
		String article = loadNewsArticle(newsId);
		Map<String, Object> metadata = loadNewsMetadata(newsId);
		model.addAttribute("article", article);
		model.addAttribute("id", newsId);
		model.addAttribute("title", metadata.get("title"));
		model.addAttribute("createdDate", metadata.get("createdDate"));
		return "news/show";
	}

	private List<Map<String, Object>> generateNewsMetadataList() {
		Yaml yaml = new Yaml();
		List<Map<String, Object>> newsMetadataList = new ArrayList<Map<String, Object>>();
		InputStream stream;
		try {
			stream = new ClassPathResource("static/yaml/news/display-order.yaml").getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return newsMetadataList;
		}
		List<Integer> newsIds = yaml.load(stream);
		newsIds.forEach((id) -> {
			Map<String, Object> news = loadNewsMetadata(id);
			newsMetadataList.add(news);
		});
		return newsMetadataList;
	}

	private Map<String, Object> loadNewsMetadata(Integer id) {
		Yaml yaml = new Yaml();
		Map<String, Object> news = new HashMap<String, Object>();
		news.put("id", id);
		InputStream stream;
		try {
			stream = new ClassPathResource("static/yaml/news/metadata-" + id + ".yaml").getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			news.put("title", "");
			news.put("shortTitle", "");
			news.put("createdDate", null);
			return news;
		}
		Map<String, String> metadata = yaml.load(stream);
		news.put("title", metadata.get("title"));
		news.put("shortTitle", metadata.get("short-title"));
		news.put("createdDate", LocalDate.parse(metadata.get("created-date")));
		return news;
	}

	private String loadNewsArticle(Integer id) {
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
		Path path = Paths.get("src/main/resources/static/md/news/" + id + ".md");
		List<String> lines;
		try {
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NotFound404();
		}
		Node document = parser.parse(lines.stream().collect(Collectors.joining("\n")));
		return renderer.render(document);
	}

	private void setHeader(Model model) {
		Account account = accountService.getAccountByEmail(securityService.findLoggedInEmail());
		if (Objects.equals(account, null)) {
			model.addAttribute("header", "for-stranger");
		} else if (account.isAdmin()) {
			model.addAttribute("header", "for-admin");
		} else {
			model.addAttribute("header", "for-user");
		}
	}
}
