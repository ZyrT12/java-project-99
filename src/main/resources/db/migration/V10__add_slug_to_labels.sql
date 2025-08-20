ALTER TABLE labels ADD COLUMN slug VARCHAR(255);

UPDATE labels SET slug = LOWER(name);

ALTER TABLE labels ALTER COLUMN slug SET NOT NULL;

CREATE UNIQUE INDEX ux_labels_slug ON labels(slug);
