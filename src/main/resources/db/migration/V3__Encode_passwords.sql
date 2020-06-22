-- noinspection SqlResolveForFile

CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE usrs SET password = crypt(password, gen_salt('bf', 8));