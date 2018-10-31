CREATE TABLE counts (
  name VARCHAR(45) NOT NULL,
  count INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (name)
) CHARACTER SET utf8;

INSERT INTO counts VALUES ('accounts', 0), ('profiles', 0), ('experiences', 0);
