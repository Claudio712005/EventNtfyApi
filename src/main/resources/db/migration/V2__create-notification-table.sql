CREATE TABLE notifications
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT    NOT NULL,
    message        TEXT      NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    sent           BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
