CREATE TABLE accounts (
  email VARCHAR(150) NOT NULL,
  password CHAR(60) NOT NULL,
  authority VARCHAR(45) NOT NULL DEFAULT 'ROLE_USER',
  valid_email BOOLEAN NOT NULL DEFAULT false,
  updated_at TIMESTAMP NULL,
  created_at TIMESTAMP NULL,
  last_login TIMESTAMP NULL,
  email_token CHAR(40) DEFAULT NULL UNIQUE,
  password_token CHAR(40) DEFAULT NULL UNIQUE,
  PRIMARY KEY (email)
) CHARACTER SET utf8 COLLATE utf8_bin;

CREATE UNIQUE INDEX ix_auth_email on accounts (email);

CREATE TABLE persistent_logins (
  username VARCHAR(255) NOT NULL,
  series VARCHAR(64) NOT NULL,
  token VARCHAR(64) NOT NULL,
  last_used TIMESTAMP NOT NULL,
  PRIMARY KEY (series)
);

INSERT INTO accounts (email, password, authority, valid_email, updated_at, created_at, last_login) VALUES ('admin@t6sdl.tokyo', '$2a$10$Y4JO/BcrS9JBPG6cJycBp.tb2ouD5ywOnxo5GXZt2h2jR81afKv1S', 'ROLE_ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO accounts (email, password, valid_email, updated_at, created_at, last_login) VALUES ('user1@t6sdl.tokyo', '$2a$10$Y4JO/BcrS9JBPG6cJycBp.tb2ouD5ywOnxo5GXZt2h2jR81afKv1S', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO accounts (email, password, valid_email, updated_at, created_at, last_login) VALUES ('user2@t6sdl.tokyo', '$2a$10$Y4JO/BcrS9JBPG6cJycBp.tb2ouD5ywOnxo5GXZt2h2jR81afKv1S', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO accounts (email, password, valid_email, updated_at, created_at, last_login) VALUES ('user3@t6sdl.tokyo', '$2a$10$Y4JO/BcrS9JBPG6cJycBp.tb2ouD5ywOnxo5GXZt2h2jR81afKv1S', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
