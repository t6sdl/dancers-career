ALTER TABLE accounts ADD new_es_mail BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE experiences ADD created_at TIMESTAMP NULL;

UPDATE accounts SET new_es_mail = true WHERE authority = 'ROLE_USER';

UPDATE experiences SET created_at = '2018-11-07 12:00:00';

UPDATE experiences SET created_at = '2018-12-04 17:00:00' WHERE experience_id = 371;
