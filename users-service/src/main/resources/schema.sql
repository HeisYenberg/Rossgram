CREATE TABLE IF NOT EXISTS collections
(
    id BIGSERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS collection_images
(
    collection_id BIGINT       NOT NULL,
    image_id      VARCHAR(255) NOT NULL,
    PRIMARY KEY (collection_id, image_id),
    FOREIGN KEY (collection_id) REFERENCES collections (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    collection_id BIGINT       NOT NULL,
    FOREIGN KEY (collection_id) REFERENCES collections (id) ON DELETE CASCADE
);
