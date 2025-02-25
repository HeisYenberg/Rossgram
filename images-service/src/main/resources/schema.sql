CREATE TABLE IF NOT EXISTS images
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      VARCHAR(255) NOT NULL,
    image_base64 TEXT         NOT NULL,
    deleted      BOOLEAN      NOT NULL
);