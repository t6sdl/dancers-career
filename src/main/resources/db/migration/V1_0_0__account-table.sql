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

INSERT INTO accounts (email, password, authority, valid_email, updated_at, created_at, last_login) VALUES ('admin@t6sdl.tokyo', '$2a$10$wKXDlrqlwfxJqwud6iT2RORZkxiK2bSq.PXRWEx21Dfaf.F5SwOn6', 'ROLE_ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO accounts (email, password, valid_email, updated_at, created_at, last_login) VALUES ('user1@t6sdl.tokyo', '$2a$10$yXtGsfu.8xbJJl/aSC.36eLqq4DXnFkx6UgeWf4thrKEwGMyApe/y', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO accounts (email, password, valid_email, updated_at, created_at, last_login) VALUES ('user2@t6sdl.tokyo', '$2a$10$HrsvTe6OTy2qm7H8zeK0A.CR9KCqSZsT70nAMxED2g6ttTVBYjoRi', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO accounts (email, password, valid_email, updated_at, created_at, last_login) VALUES ('user3@t6sdl.tokyo', '$2a$10$f0ZkKhEkhTl8hTAfxcwyou9wM9OuaQTpKJSa//QEikdfItZqBOFwC', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
