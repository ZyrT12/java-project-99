ALTER TABLE labels ADD COLUMN created_at_ts TIMESTAMP WITH TIME ZONE;
UPDATE labels SET created_at_ts = CAST(created_at AS TIMESTAMP);
ALTER TABLE labels ALTER COLUMN created_at_ts SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE labels ALTER COLUMN created_at_ts SET NOT NULL;
ALTER TABLE labels DROP COLUMN created_at;
ALTER TABLE labels RENAME COLUMN created_at_ts TO created_at;
