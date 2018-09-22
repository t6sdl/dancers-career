package tokyo.t6sdl.dancerscareer2019.io;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
		List<Student> students = (List<Student>) model.get("students");
		List<String> header = Arrays.asList("メールアドレス", "認証可否", "最終ログイン日時", "姓", "名", "セイ", "メイ", "生年月日", "性別", "電話番号", "文理", "大学所在地", "大学", "学部", "学科", "卒業年度", "最終学歴", "役職", "お気に入りのES/体験記");
		
		Sheet studentsSheet = workbook.createSheet("登録者情報【取扱注意】");
		studentsSheet.setDefaultColumnWidth(15);
		
		Row studentsRow = studentsSheet.createRow(0);
		for (int i = 0; i < header.size(); i++) {
			studentsRow.createCell(i).setCellValue(header.get(i));
		}
		
		for (int j = 0; j < students.size(); j++) {
			studentsRow = studentsSheet.createRow(j + 1);
			Student student = students.get(j);
			String validEmail = student.isValid_email() ? "確認済" : "未確認";
			String lastLogin = student.getLast_login().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
			String birth = student.getDate_of_birth().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			String position = this.listToString(student.getPosition());
			String likes = this.listToString(student.getLikes());
			List<String> studentData = Arrays.asList(student.getEmail(), validEmail, lastLogin, student.getLast_name(), student.getFirst_name(), student.getKana_last_name(), student.getKana_first_name(), birth, student.getSex(), student.getPhone_number(), student.getMajor(), student.getPrefecture(), student.getUniversity(), student.getFaculty(), student.getDepartment(), student.getGraduation(), student.getAcademic_degree(), position, likes);
			for (int k = 0; k < studentData.size(); k++) {
				studentsRow.createCell(k).setCellValue(studentData.get(k));
			}
		}
	}

	private String listToString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			builder.append(list.get(i));
			if (i < list.size() - 1) {
				builder.append(",");
			}
		}
		String str = builder.toString();
		return str;
	}
}
