DO $$ BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'users' AND column_name = 'created_at' AND data_type = 'date'
  ) THEN
    EXECUTE 'ALTER TABLE users ALTER COLUMN created_at TYPE timestamp with time zone USING (CAST(created_at AS timestamp))';
EXECUTE 'ALTER TABLE users ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP';
EXECUTE 'ALTER TABLE users ALTER COLUMN created_at SET NOT NULL';
END IF;
END $$;

DO $$ BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'task_statuses' AND column_name = 'created_at' AND data_type = 'date'
  ) THEN
    EXECUTE 'ALTER TABLE task_statuses ALTER COLUMN created_at TYPE timestamp with time zone USING (CAST(created_at AS timestamp))';
EXECUTE 'ALTER TABLE task_statuses ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP';
EXECUTE 'ALTER TABLE task_statuses ALTER COLUMN created_at SET NOT NULL';
END IF;
END $$;

DO $$ BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'tasks' AND column_name = 'created_at' AND data_type = 'date'
  ) THEN
    EXECUTE 'ALTER TABLE tasks ALTER COLUMN created_at TYPE timestamp with time zone USING (CAST(created_at AS timestamp))';
EXECUTE 'ALTER TABLE tasks ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP';
EXECUTE 'ALTER TABLE tasks ALTER COLUMN created_at SET NOT NULL';
END IF;
END $$;
