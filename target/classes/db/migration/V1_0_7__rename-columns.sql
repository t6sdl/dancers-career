SET AUTOCOMMIT = 0;
BEGIN;

ALTER TABLE senior_positions
  DROP PRIMARY KEY,
  DROP FOREIGN KEY senior_positions_ibfk_1,
  MODIFY position VARCHAR(30) NOT NULL,
  CHANGE id exp_id INTEGER UNSIGNED NOT NULL;

ALTER TABLE es
  DROP PRIMARY KEY,
  DROP FOREIGN KEY es_ibfk_1,
  CHANGE id exp_id INTEGER UNSIGNED NOT NULL,
  CHANGE es_id id INTEGER UNSIGNED NOT NULL;

ALTER TABLE interview
  DROP PRIMARY KEY,
  DROP FOREIGN KEY interview_ibfk_1,
  CHANGE id exp_id INTEGER UNSIGNED NOT NULL,
  CHANGE interview_id id INTEGER UNSIGNED NOT NULL;

ALTER TABLE experiences
  DROP PRIMARY KEY,
  DROP KEY ix_name,
  DROP KEY ix_univ,
  CHANGE experience_id id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  CHANGE last_name family_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE first_name given_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE kana_last_name kana_family_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE kana_first_name kana_given_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE univ_pref univ_loc VARCHAR(30) NOT NULL DEFAULT '',
  CHANGE faculty univ_fac VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE department univ_dep VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE grad_school_pref grad_loc VARCHAR(30) NOT NULL DEFAULT '',
  CHANGE grad_school_name grad_name VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE grad_school_of grad_school VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE program_in grad_div VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE graduation graduated_in CHAR(4) NOT NULL DEFAULT '',
  CHANGE academic_degree degree VARCHAR(15) NOT NULL DEFAULT '',
  ADD PRIMARY KEY (id),
  ADD KEY ix_name (kana_family_name, kana_given_name),
  ADD KEY ix_univ (univ_loc, univ_name, univ_fac, univ_dep);

ALTER TABLE senior_positions
  ADD PRIMARY KEY (position, exp_id),
  ADD FOREIGN KEY (exp_id) REFERENCES experiences (id) ON DELETE CASCADE;

ALTER TABLE es
  ADD PRIMARY KEY (exp_id, id),
  ADD FOREIGN KEY (exp_id) REFERENCES experiences (id) ON DELETE CASCADE;

ALTER TABLE interview
  ADD PRIMARY KEY (exp_id, id),
  ADD FOREIGN KEY (exp_id) REFERENCES experiences (id) ON DELETE CASCADE;

ALTER TABLE profiles
  DROP KEY ix_name,
  DROP KEY ix_univ,
  CHANGE last_name family_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE first_name given_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE kana_last_name kana_family_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE kana_first_name kana_given_name VARCHAR(45) NOT NULL DEFAULT '',
  CHANGE phone_number phone VARCHAR(15) NOT NULL DEFAULT '',
  CHANGE univ_pref univ_loc VARCHAR(30) NOT NULL DEFAULT '',
  CHANGE faculty univ_fac VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE department univ_dep VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE grad_school_pref grad_loc VARCHAR(30) NOT NULL DEFAULT '',
  CHANGE grad_school_name grad_name VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE grad_school_of grad_school VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE program_in grad_div VARCHAR(50) NOT NULL DEFAULT '',
  CHANGE graduation graduated_in CHAR(7) NOT NULL DEFAULT '',
  CHANGE academic_degree degree VARCHAR(15) NOT NULL DEFAULT '',
  ADD KEY ix_name (kana_family_name, kana_given_name),
  ADD KEY ix_univ (univ_loc, univ_name, univ_fac, univ_dep);

ALTER TABLE positions
  MODIFY position VARCHAR(30) NOT NULL;

COMMIT;
SET AUTOCOMMIT = 1;