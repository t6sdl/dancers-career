ALTER TABLE accounts ADD INDEX ix_new_es_mail (new_es_mail);

ALTER TABLE experiences ADD INDEX ix_created_at (created_at);
