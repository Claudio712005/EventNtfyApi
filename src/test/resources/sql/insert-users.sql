MERGE INTO users (id, username, email, created_at, updated_at)
    VALUES
    (1, 'Alice', 'alice@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'Bob', 'bob@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE users ALTER COLUMN id RESTART WITH 3;