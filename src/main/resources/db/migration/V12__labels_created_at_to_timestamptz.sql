ALTER TABLE labels
    ALTER COLUMN created_at DROP DEFAULT;

ALTER TABLE labels
ALTER COLUMN created_at TYPE timestamptz
    USING (created_at::timestamp AT TIME ZONE 'UTC');

ALTER TABLE labels
    ALTER COLUMN created_at SET DEFAULT NOW(),
ALTER COLUMN created_at SET NOT NULL;
