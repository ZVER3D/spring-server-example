DELETE FROM user_role;
DELETE FROM usrs;

INSERT INTO usrs (id, password, username, active)
VALUES (1, '$2a$08$BAisHH52C73nd75YEB9bJOULSX51mCKxoi7GCaF22CMRMLuuLl4tG', 'u3', true),
       (2, '$2a$08$BAisHH52C73nd75YEB9bJOULSX51mCKxoi7GCaF22CMRMLuuLl4tG', 'u2', true);

INSERT INTO user_role (user_id, roles)
VALUES (1, 'USER'),
       (1, 'ADMIN'),
       (2, 'USER');