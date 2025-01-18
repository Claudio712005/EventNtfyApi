CREATE TABLE notifications
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT    NOT NULL,
    message        TEXT      NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    sent           BOOLEAN     DEFAULT FALSE,
    status         VARCHAR(50) DEFAULT 'PENDING',
    priority       INT         DEFAULT 1,
    type           VARCHAR(50) DEFAULT 'EMAIL',
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);