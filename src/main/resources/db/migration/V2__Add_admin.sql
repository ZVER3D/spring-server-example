-- noinspection SqlResolveForFile

INSERT INTO usrs (id, username, password, email, active)
VALUES (1, 'admin', '123', 'adming@example.com', true);

INSERT INTO user_role (user_id, roles)
VALUES (1, 'USER'),
       (1, 'ADMIN');