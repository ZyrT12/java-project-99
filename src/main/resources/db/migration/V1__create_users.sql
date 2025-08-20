CREATE TABLE IF NOT EXISTS users (
                                     id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                     email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    created_at DATE NOT NULL DEFAULT CURRENT_DATE
    );

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
