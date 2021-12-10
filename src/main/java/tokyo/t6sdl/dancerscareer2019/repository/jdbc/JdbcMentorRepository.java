package tokyo.t6sdl.dancerscareer2019.repository.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import tokyo.t6sdl.dancerscareer2019.model.Mentor;
import tokyo.t6sdl.dancerscareer2019.model.Mentor.AdditionalMentorInfo;
import tokyo.t6sdl.dancerscareer2019.repository.MentorRepository;

@RequiredArgsConstructor
@Repository
public class JdbcMentorRepository implements MentorRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	
	@AllArgsConstructor
	private static class MentorsTable {
		private int id;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private String familyNameJa;
		private String givenNameJa;
		private String familyNameEn;
		private String givenNameEn;
		private String worksAt;
		private String graduatedFrom;
		private String danceClub;
		private byte[] image;
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static List<Field> getAllFields() {
			return new ArrayList(Arrays.asList(MentorsTable.class.getDeclaredFields()));
		}
		
		private static List<Field> getFieldsExceptId() {
			return getAllFields().stream().filter(field -> field.getName() != "id").collect(Collectors.toList());
		}
		
		private static MentorsTable fromResultSet(ResultSet resultSet) throws SQLException {
			int id = resultSet.getInt("id");
			LocalDateTime createdAt = LocalDateTime.ofInstant(resultSet.getTimestamp("created_at").toInstant(), ZoneId.of("Asia/Tokyo"));
			LocalDateTime updatedAt = LocalDateTime.ofInstant(resultSet.getTimestamp("updated_at").toInstant(), ZoneId.of("Asia/Tokyo"));
			String familyNameJa = resultSet.getString("family_name_ja");
			String givenNameJa = resultSet.getString("given_name_ja");
			String familyNameEn = resultSet.getString("family_name_en");
			String givenNameEn = resultSet.getString("given_name_en");
			String worksAt = resultSet.getString("works_at");
			String graduatedFrom = resultSet.getString("graduated_from");
			String danceClub = resultSet.getString("dance_club");
			InputStream streamImage = resultSet.getBinaryStream("image");
			byte[] binaryImage = binaryFromStream(streamImage);
			return new MentorsTable(id, createdAt, updatedAt, familyNameJa, givenNameJa, familyNameEn, givenNameEn, worksAt, graduatedFrom, danceClub, binaryImage);
		}
		
		private static MentorsTable fromModel(Mentor mentor) {
			return new MentorsTable(
				mentor.getId(),
				mentor.getCreatedAt(),
				mentor.getUpdatedAt(),
				mentor.getFamilyNameJa(),
				mentor.getGivenNameJa(),
				mentor.getFamilyNameEn(),
				mentor.getGivenNameEn(),
				mentor.getWorksAt(),
				mentor.getGraduatedFrom(),
				mentor.getDanceClub(),
				mentor.getBinaryImage()
			);
		}
		
		private static byte[] binaryFromStream(InputStream stream) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			try {
				while (stream.read(data) != -1) {
					buffer.write(data);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return buffer.toByteArray();
		}

		private Mentor toModelWith(List<AdditionalMentorInfosTable> mentorInfos) {
			Mentor model = new Mentor();
			model.setId(id);
			model.setCreatedAt(createdAt);
			model.setUpdatedAt(updatedAt);
			model.setFamilyNameJa(familyNameJa);
			model.setGivenNameJa(givenNameJa);
			model.setFamilyNameEn(familyNameEn);
			model.setGivenNameEn(givenNameEn);
			model.setWorksAt(worksAt);
			model.setGraduatedFrom(graduatedFrom);
			model.setDanceClub(danceClub);
			model.setImageFromBinary(image);
			mentorInfos.forEach(info -> {
				model.appendAdditionalMentorInfo(info.id, info.createdAt, info.updatedAt, info.title, info.content);
			});
			return model;
		}
				
		private SqlParameterSource recordValuesMap() {
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			if (!Objects.equals(id, null)) {
				paramSource.addValue("id", id);
			}
			if (!Objects.equals(createdAt, null)) {
				paramSource.addValue("createdAt", createdAt);
			} else {
				paramSource.addValue("createdAt", LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
			}
			if (!Objects.equals(updatedAt, null)) {
				paramSource.addValue("updatedAt", updatedAt);
			} else {
				paramSource.addValue("updatedAt", LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
			}
			paramSource.addValue("familyNameJa", familyNameJa);
			paramSource.addValue("givenNameJa", givenNameJa);
			paramSource.addValue("familyNameEn", familyNameEn);
			paramSource.addValue("givenNameEn", givenNameEn);
			paramSource.addValue("worksAt", worksAt);
			paramSource.addValue("graduatedFrom", graduatedFrom);
			paramSource.addValue("danceClub", danceClub);
			paramSource.addValue("image", image);
			return paramSource;
		}
	}
	
	@AllArgsConstructor
	private static class AdditionalMentorInfosTable {
		private int id;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private String title;
		private String content;
		private int mentorId;
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static List<Field> getAllFields() {
			return new ArrayList(Arrays.asList(AdditionalMentorInfosTable.class.getDeclaredFields()));
		}
		
		private static List<Field> getFieldsExceptId() {
			return getAllFields().stream().filter(field -> field.getName() != "id").collect(Collectors.toList());
		}
		
		private static AdditionalMentorInfosTable fromResultSet(ResultSet resultSet) throws SQLException {
			int id = resultSet.getInt("id");
			LocalDateTime createdAt = LocalDateTime.ofInstant(resultSet.getTimestamp("created_at").toInstant(), ZoneId.of("Asia/Tokyo"));
			LocalDateTime updatedAt = LocalDateTime.ofInstant(resultSet.getTimestamp("updated_at").toInstant(), ZoneId.of("Asia/Tokyo"));
			String title = resultSet.getString("title");
			String content = resultSet.getString("content");
			int mentorId = resultSet.getInt("mentor_id");
			return new AdditionalMentorInfosTable(id, createdAt, updatedAt, title, content, mentorId);
		}
		
		private static List<AdditionalMentorInfosTable> fromMentorModel(Mentor mentor) {
			return mentor.getAdditionalMentorInfos().stream().map(info -> {
				return AdditionalMentorInfosTable.fromModel(info, mentor.getId());
			}).collect(Collectors.toList());
		}
		
		private static AdditionalMentorInfosTable fromModel(AdditionalMentorInfo info, int mentorId) {
			return new AdditionalMentorInfosTable(info.getId(), info.getCreatedAt(), info.getUpdatedAt(), info.getTitle(), info.getContent(), mentorId);
		}
		
		private SqlParameterSource recordValuesMap() {
			MapSqlParameterSource paramSource = new MapSqlParameterSource();
			if (Objects.equals(id, null)) {
				paramSource.addValue("id", id);
			}
			if (!Objects.equals(createdAt, null)) {
				paramSource.addValue("createdAt", createdAt);
			} else {
				paramSource.addValue("createdAt", LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
			}
			if (!Objects.equals(updatedAt, null)) {
				paramSource.addValue("updatedAt", updatedAt);
			} else {
				paramSource.addValue("updatedAt", LocalDateTime.now(ZoneId.of("Asia/Tokyo")));
			}
			paramSource.addValue("title", title);
			paramSource.addValue("content", content);
			paramSource.addValue("mentorId", mentorId);
			return paramSource;
		}
	}

	@Override
	public List<Mentor> getAll() {
		List<AdditionalMentorInfosTable> mentorInfoRecords = jdbcTemplate.query("SELECT * FROM additional_mentor_informations ORDER BY mentor_id, id", (resultSet, i) -> {
			AdditionalMentorInfosTable mentorInfoRecord = AdditionalMentorInfosTable.fromResultSet(resultSet);
			return mentorInfoRecord;
		});
		List<Mentor> mentors = jdbcTemplate.query("SELECT * FROM mentors ORDER BY id", (resultSet, i) -> {
			MentorsTable mentorRecord = MentorsTable.fromResultSet(resultSet);
			List<AdditionalMentorInfosTable> associatedInfos = mentorInfoRecords.stream().filter(record -> record.mentorId == mentorRecord.id).collect(Collectors.toList());
			return mentorRecord.toModelWith(associatedInfos);
		});
		return mentors;
	}

	@Override
	public Mentor find(int id) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", id);
		List<AdditionalMentorInfosTable> mentorInfoRecords = jdbcTemplate.query("SELECT * FROM additional_mentor_informations WHERE mentor_id = :id ORDER BY mentor_id, id", params, (resultSet, i) -> {
			AdditionalMentorInfosTable mentorInfoRecord = AdditionalMentorInfosTable.fromResultSet(resultSet);
			return mentorInfoRecord;
		});
		Mentor mentor = jdbcTemplate.queryForObject("SELECT * FROM mentors WHERE id = :id", params, (resultSet, i) -> {
			MentorsTable mentorRecord = MentorsTable.fromResultSet(resultSet);
			List<AdditionalMentorInfosTable> associatedInfos = mentorInfoRecords.stream().filter(record -> record.mentorId == mentorRecord.id).collect(Collectors.toList());
			return mentorRecord.toModelWith(associatedInfos);
		});
		return mentor;
	}

	@Override
	public void create(Mentor mentor) {
		MentorsTable mentorRecord = MentorsTable.fromModel(mentor);
		SqlParameterSource mentorValues = mentorRecord.recordValuesMap();
		int mentorId = insertMentorsTable(mentorValues);
		mentor.setId(mentorId);
		List<AdditionalMentorInfosTable> mentorInfoRecords = AdditionalMentorInfosTable.fromMentorModel(mentor);
		List<SqlParameterSource> mentorInfosValues = mentorInfoRecords.stream().map(info -> info.recordValuesMap()).collect(Collectors.toList());
		mentorInfosValues.forEach(mentorInfosValue -> insertAdditionalMentorInfosTable(mentorInfosValue));
	}

	@Override
	public void update(Mentor mentor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(int id) {
		this.deleteAdditionalMentorInfosTable(id);
		this.deleteMentorsTable(id);
	}
	
	private int insertStatement(String table, List<Field> fields, SqlParameterSource paramSource) {
		List<String> camelCaseFieldNames = fields.stream().map(field -> field.getName()).collect(Collectors.toList());
		List<String> snakeCaseFieldNames = camelCaseFieldNames.stream().map(fieldName -> fieldName.replaceAll("([a-z0-9]+)([A-Z]+)", "$1_$2").toLowerCase()).collect(Collectors.toList());
		String sql = "INSERT INTO " + table + " (" + String.join(", ", snakeCaseFieldNames) + ")"
				+ " VALUES (" + camelCaseFieldNames.stream().map(fieldName -> ":" + fieldName).collect(Collectors.joining(", ")) + ")";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(sql, paramSource, keyHolder);
		int key = Integer.parseInt(keyHolder.getKey().toString());
		return key;
	}

	private int insertMentorsTable(SqlParameterSource paramSource) {
		return insertStatement("mentors", MentorsTable.getFieldsExceptId(), paramSource);
	}
	
	private int insertAdditionalMentorInfosTable(SqlParameterSource paramSource) {
		return insertStatement("additional_mentor_informations", AdditionalMentorInfosTable.getFieldsExceptId(), paramSource);
	}
	
	private boolean deleteStatement(String table, String key, int id) {
		String sql = "DELETE FROM " + table + " WHERE " + key + " = :id";
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("id", id);
		jdbcTemplate.update(sql, paramMap);
		return true;
	}
	
	private boolean deleteMentorsTable(int id) {
		return this.deleteStatement("mentors", "id", id);
	}
	
	private boolean deleteAdditionalMentorInfosTable(int mentor_id) {
		return this.deleteStatement("additional_mentor_informations", "mentor_id", mentor_id);
	}
}
