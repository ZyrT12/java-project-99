ALTER TABLE labels
ALTER COLUMN created_at TYPE timestamp with time zone
  USING (CAST(created_at AS timestamp));

ALTER TABLE labels
    ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE labels
    ALTER COLUMN created_at SET NOT NULL;
