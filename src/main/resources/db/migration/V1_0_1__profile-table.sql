CREATE TABLE profiles (
  email VARCHAR(150) NOT NULL COLLATE utf8_bin,
  last_name VARCHAR(45) NOT NULL,
  first_name VARCHAR(45) NOT NULL,
  kana_last_name VARCHAR(45) NOT NULL,
  kana_first_name VARCHAR(45) NOT NULL,
  date_of_birth DATE NOT NULL,
  sex ENUM('男性', '女性') NOT NULL,
  phone_number VARCHAR(15) NOT NULL,
  major ENUM('文系', '理系') NOT NULL,
  univ_pref VARCHAR(30) NOT NULL,
  univ_name VARCHAR(50) NOT NULL,
  faculty VARCHAR(50) NOT NULL,
  department VARCHAR(50) NOT NULL,
  grad_school_pref VARCHAR(30) NOT NULL DEFAULT '',
  grad_school_name VARCHAR(50) NOT NULL DEFAULT '',
  grad_school_of VARCHAR(50) NOT NULL DEFAULT '',
  program_in VARCHAR(50) NOT NULL DEFAULT '',
  graduation CHAR(7) NOT NULL,
  academic_degree VARCHAR(15) NOT NULL,
  position VARCHAR(150) NOT NULL,
  likes VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (email),
  KEY ix_name (kana_last_name, kana_first_name),
  KEY ix_univ (prefecture, university, faculty, department),
  FOREIGN KEY (email) REFERENCES accounts (email) ON DELETE CASCADE ON UPDATE CASCADE
) CHARACTER SET utf8;

CREATE TABLE positions (
  position ENUM('代表', '副代表', '会計', '広報', '渉外・営業', 'ジャンルリーダー', '振付師', '公演舞台監督', '公演総合演出', '公演ストーリー制作', '音響制作', '照明制作', '映像制作', '衣装制作', 'イベントオーガナイザー', '新歓係', '合宿統括', '役職なし', 'その他') NOT NULL,
  email VARCHAR(150) NOT NULL COLLATE utf8_bin,
  PRIMARY KEY (position, email),
  FOREIGN KEY (email) REFERENCES profiles (email) ON DELETE CASCADE ON UPDATE CASCADE
) CHARACTER SET utf8;
