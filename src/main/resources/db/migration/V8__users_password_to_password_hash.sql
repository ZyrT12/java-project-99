ALTER TABLE users
    ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255);

UPDATE users
SET password_hash = password
WHERE password_hash IS NULL;

ALTER TABLE users
DROP COLUMN IF EXISTS password;

ALTER TABLE users
    ALTER COLUMN password_hash SET NOT NULL;
