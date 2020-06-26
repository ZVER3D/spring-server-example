DELETE FROM message;

INSERT INTO message (id, text, tag, user_id)
VALUES (1, 'first', 'my-tag', 1),
       (2, 'second', 'my-tag', 2),
       (3, 'third', 'my-tag', 1),
       (4, 'fourth', 'pog', 2);

ALTER SEQUENCE hibernate_sequence RESTART WITH 10;