CREATE TABLE IF NOT EXISTS task_statuses (
                                             id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                             name       VARCHAR(255) NOT NULL,
    slug       VARCHAR(255) NOT NULL UNIQUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE
    );

CREATE INDEX IF NOT EXISTS idx_task_statuses_slug ON task_statuses(slug);
