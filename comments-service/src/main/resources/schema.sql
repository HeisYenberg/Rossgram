CREATE TABLE IF NOT EXISTS comments
(
    id       BIGSERIAL PRIMARY KEY,
    image_id VARCHAR(255) NOT NULL,
    user_id  VARCHAR(255) NOT NULL,
    text     TEXT         NOT NULL,
    deleted  BOOLEAN      NOT NULL
);