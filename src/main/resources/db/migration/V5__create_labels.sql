CREATE TABLE IF NOT EXISTS labels (
                                      id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                      name       VARCHAR(1000) NOT NULL UNIQUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE
    );

CREATE UNIQUE INDEX IF NOT EXISTS ux_labels_name ON labels (LOWER(name));
