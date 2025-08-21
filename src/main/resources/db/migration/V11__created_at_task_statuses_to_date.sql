ALTER TABLE task_statuses
    ALTER COLUMN created_at DROP DEFAULT;

UPDATE task_statuses
SET created_at = CURRENT_DATE
WHERE created_at IS NULL;

ALTER TABLE task_statuses
    ALTER COLUMN created_at TYPE date USING created_at::date;

ALTER TABLE task_statuses
    ALTER COLUMN created_at SET DEFAULT CURRENT_DATE;

ALTER TABLE task_statuses
    ALTER COLUMN created_at SET NOT NULL;
