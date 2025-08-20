INSERT INTO labels (name, created_at)
SELECT 'feature', CURRENT_DATE
    WHERE NOT EXISTS (SELECT 1 FROM labels WHERE LOWER(name) = LOWER('feature'));

INSERT INTO labels (name, created_at)
SELECT 'bug', CURRENT_DATE
    WHERE NOT EXISTS (SELECT 1 FROM labels WHERE LOWER(name) = LOWER('bug'));
