package tokyo.t6sdl.dancerscareer2019.io;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Student;

@RequiredArgsConstructor
public class ExcelBuilder extends AbstractXlsxView {
	private final String fileName;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		
		@SuppressWarnings("unchecked")
		List<String> filter = (List<String>) model.get("filter");
		@SuppressWarnings("unchecked")
		List<Student> students = (List<Student>) model.get("students");
		List<String> header = Arrays.asList("メールアドレス", "認証", "最終ログイン日時", "姓", "名", "セイ", "メイ", "生年月日", "性別", "電話番号", "卒業年度", "最終学歴", "文理", "大学所在地", "大学", "学部", "学科", "大学院所在地", "大学院", "研究科", "専攻", "サークル", "役職", "お気に入りのES/体験記");
		
		Sheet filterSheet = workbook.createSheet("検索条件");
		filterSheet.setDefaultColumnWidth(15);
		
		Row filterRow = filterSheet.createRow(0);
		filterRow.createCell(0).setCellValue("ヒット件数");
		filterRow.createCell(1).setCellValue("検索条件");
		filterRow.createCell(2).setCellValue("内容");
		filterRow = filterSheet.createRow(1);
		filterRow.createCell(0).setCellValue(students.size());
		filterRow.createCell(1).setCellValue(filter.get(0));
		filterRow.createCell(2).setCellValue(filter.get(1));
		
		Sheet studentsSheet = workbook.createSheet("登録者情報【取扱注意】");
		studentsSheet.setDefaultColumnWidth(15);
		
		Row studentsRow = studentsSheet.createRow(0);
		for (int i = 0; i < header.size(); i++) {
			studentsRow.createCell(i).setCellValue(header.get(i));
		}
		
		int i = 1;
		for (Student student : students) {
			studentsRow = studentsSheet.createRow(i++);
			String validEmail = student.isValidEmail() ? "済" : "未";
			String lastLogin = student.getLastLogin().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
			String birth;
			String positions;
			String likes;
			if (Objects.equals(student.getBirth(), null)) {
				birth = "";
				positions = "";
				likes = "";
			} else {
				birth = student.getBirth().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
				positions = student.getPosition().stream().collect(Collectors.joining(","));
				likes = student.getLikes().stream().collect(Collectors.joining(","));
			}
			List<String> studentData = Arrays.asList(student.getEmail(), validEmail, lastLogin, student.getFamilyName(), student.getGivenName(), student.getKanaFamilyName(), student.getKanaGivenName(), birth, student.getSex(), student.getPhone(), student.getGraduatedIn(), student.getDegree(), student.getMajor(), student.getUnivLoc(), student.getUnivName(), student.getUnivFac(), student.getUnivDep(), student.getGradLoc(), student.getGradName(), student.getGradSchool(), student.getGradDiv(), student.getClub(), positions, likes);
			for (int j = 0; j < studentData.size(); j++) {
				studentsRow.createCell(j).setCellValue(studentData.get(j));
			}
		}
	}
}
