ALTER TABLE users ADD COLUMN created_at_ts TIMESTAMP WITH TIME ZONE;
UPDATE users SET created_at_ts = CAST(created_at AS TIMESTAMP);
ALTER TABLE users ALTER COLUMN created_at_ts SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE users ALTER COLUMN created_at_ts SET NOT NULL;
ALTER TABLE users DROP COLUMN created_at;
ALTER TABLE users RENAME COLUMN created_at_ts TO created_at;

ALTER TABLE task_statuses ADD COLUMN created_at_ts TIMESTAMP WITH TIME ZONE;
UPDATE task_statuses SET created_at_ts = CAST(created_at AS TIMESTAMP);
ALTER TABLE task_statuses ALTER COLUMN created_at_ts SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE task_statuses ALTER COLUMN created_at_ts SET NOT NULL;
ALTER TABLE task_statuses DROP COLUMN created_at;
ALTER TABLE task_statuses RENAME COLUMN created_at_ts TO created_at;

ALTER TABLE tasks ADD COLUMN created_at_ts TIMESTAMP WITH TIME ZONE;
UPDATE tasks SET created_at_ts = CAST(created_at AS TIMESTAMP);
ALTER TABLE tasks ALTER COLUMN created_at_ts SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tasks ALTER COLUMN created_at_ts SET NOT NULL;
ALTER TABLE tasks DROP COLUMN created_at;
ALTER TABLE tasks RENAME COLUMN created_at_ts TO created_at;
