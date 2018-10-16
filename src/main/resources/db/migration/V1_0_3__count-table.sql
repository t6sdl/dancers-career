CREATE TABLE counts (
  name VARCHAR(45) NOT NULL,
  count INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (name)
) CHARACTER SET utf8;

INSERT INTO counts VALUES ('accounts', 3), ('profiles', 3), ('experiences', 3);
