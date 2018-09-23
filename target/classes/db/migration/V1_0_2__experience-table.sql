CREATE TABLE experiences (
  experience_id INTEGER NOT NULL AUTO_INCREMENT,
  page_view INTEGER NOT NULL DEFAULT 0,
  likes INTEGER NOT NULL DEFAULT 0,
  last_name VARCHAR(45) NOT NULL DEFAULT '',
  first_name VARCHAR(45) NOT NULL DEFAULT '',
  kana_last_name VARCHAR(45) NOT NULL DEFAULT '',
  kana_first_name VARCHAR(45) NOT NULL DEFAULT '',
  sex ENUM('男性', '女性', '') NOT NULL DEFAULT '',
  major ENUM('文系', '理系', '') NOT NULL DEFAULT '',
  prefecture VARCHAR(30) NOT NULL DEFAULT '',
  university VARCHAR(255) NOT NULL DEFAULT '',
  faculty VARCHAR(255) NOT NULL DEFAULT '',
  department VARCHAR(255) NOT NULL DEFAULT '',
  graduation CHAR(4) NOT NULL DEFAULT '',
  academic_degree VARCHAR(15) NOT NULL DEFAULT '',
  position VARCHAR(315) NOT NULL DEFAULT '',
  club VARCHAR(255) NOT NULL DEFAULT '',
  offer VARCHAR(315) NOT NULL DEFAULT '',
  PRIMARY KEY (experience_id),
  KEY ix_name (kana_last_name, kana_first_name),
  KEY ix_univ (university, faculty),
  Key ix_pos (position)
);

CREATE TABLE es (
  experience_id INTEGER NOT NULL,
  es_id INTEGER NOT NULL,
  corp VARCHAR(150) NOT NULL DEFAULT '',
  result VARCHAR(75) NOT NULL DEFAULT '',
  question VARCHAR(315) NOT NULL,
  answer TEXT NOT NULL,
  advice TEXT NOT NULL,
  PRIMARY KEY (experience_id, es_id),
  FOREIGN KEY (experience_id) REFERENCES experiences (experience_id) ON DELETE CASCADE
);

CREATE TABLE interview (
  experience_id INTEGER NOT NULL,
  interview_id INTEGER NOT NULL,
  question VARCHAR(315) NOT NULL,
  answer TEXT NOT NULL,
  PRIMARY KEY (experience_id, interview_id),
  FOREIGN KEY (experience_id) REFERENCES experiences (experience_id) ON DELETE CASCADE
);

INSERT INTO experiences (prefecture, university, faculty, graduation, position) VALUES
('東京都', '東京大学', '理学部', '2018', '代表,会計,イベント企画'),
('東京都', '慶應義塾大学', '法学部', '2018', '公演総合演出,音響制作'),
('神奈川県', '横浜国立大学', '', '2018', '副代表');
