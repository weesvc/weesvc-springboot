CREATE TABLE places
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR UNIQUE NOT NULL,
    description VARCHAR,
    latitude    REAL,
    longitude   REAL,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP      NOT NULL
);
