CREATE TABLE counts (
  key VARCHAR(45) NOT NULL,
  value INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (key)
) CHARACTER SET utf8;

INSERT INTO counts VALUES ('accounts', 3), ('profiles', 3), ('experiences', 3);
