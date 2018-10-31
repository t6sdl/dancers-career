CREATE TABLE experiences (
  experience_id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  page_view INTEGER UNSIGNED NOT NULL DEFAULT 0,
  likes INTEGER UNSIGNED NOT NULL DEFAULT 0,
  last_name VARCHAR(45) NOT NULL DEFAULT '',
  first_name VARCHAR(45) NOT NULL DEFAULT '',
  kana_last_name VARCHAR(45) NOT NULL DEFAULT '',
  kana_first_name VARCHAR(45) NOT NULL DEFAULT '',
  sex ENUM('男性', '女性', '') NOT NULL DEFAULT '',
  major ENUM('文系', '理系', '') NOT NULL DEFAULT '',
  univ_pref VARCHAR(30) NOT NULL DEFAULT '',
  univ_name VARCHAR(50) NOT NULL DEFAULT '',
  faculty VARCHAR(50) NOT NULL DEFAULT '',
  department VARCHAR(50) NOT NULL DEFAULT '',
  grad_school_pref VARCHAR(30) NOT NULL DEFAULT '',
  grad_school_name VARCHAR(50) NOT NULL DEFAULT '',
  grad_school_of VARCHAR(50) NOT NULL DEFAULT '',
  program_in VARCHAR(50) NOT NULL DEFAULT '',
  graduation CHAR(4) NOT NULL DEFAULT '',
  academic_degree VARCHAR(15) NOT NULL DEFAULT '',
  position VARCHAR(150) NOT NULL DEFAULT '',
  club VARCHAR(255) NOT NULL DEFAULT '',
  offer VARCHAR(315) NOT NULL DEFAULT '',
  PRIMARY KEY (experience_id),
  KEY ix_name (kana_last_name, kana_first_name),
  KEY ix_univ (prefecture, university, faculty, department)
) CHARACTER SET utf8;

CREATE TABLE senior_positions (
  position ENUM('代表', '副代表', '会計', '広報', '渉外・営業', 'ジャンルリーダー', '振付師', '公演舞台監督', '公演総合演出', '公演ストーリー制作', '音響制作', '照明制作', '映像制作', '衣装制作', 'イベントオーガナイザー', '新歓係', '合宿統括', '役職なし', 'その他') NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY (position, id),
  FOREIGN KEY (id) REFERENCES experiences (experience_id) ON DELETE CASCADE
) CHARACTER SET utf8;

CREATE TABLE es (
  id INTEGER UNSIGNED NOT NULL,
  es_id INTEGER UNSIGNED NOT NULL,
  corp VARCHAR(150) NOT NULL DEFAULT '',
  result VARCHAR(75) NOT NULL DEFAULT '',
  question VARCHAR(315) NOT NULL,
  answer TEXT NOT NULL,
  advice TEXT NOT NULL,
  PRIMARY KEY (id, es_id),
  KEY ix_corp (corp),
  FOREIGN KEY (id) REFERENCES experiences (experience_id) ON DELETE CASCADE
) CHARACTER SET utf8;

CREATE TABLE interview (
  id INTEGER UNSIGNED NOT NULL,
  interview_id INTEGER UNSIGNED NOT NULL,
  question VARCHAR(315) NOT NULL,
  answer TEXT NOT NULL,
  PRIMARY KEY (id, interview_id),
  FOREIGN KEY (id) REFERENCES experiences (experience_id) ON DELETE CASCADE
) CHARACTER SET utf8;
