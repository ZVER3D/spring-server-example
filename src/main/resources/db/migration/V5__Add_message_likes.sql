CREATE TABLE message_likes
(
    user_id    int8 not null references usrs,
    message_id int8 not null references message,
    PRIMARY KEY (user_id, message_id)
);