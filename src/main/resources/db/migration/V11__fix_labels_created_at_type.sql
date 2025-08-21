ALTER TABLE public.labels
ALTER COLUMN created_at TYPE timestamptz
    USING (created_at::timestamp AT TIME ZONE 'UTC');

ALTER TABLE public.labels
    ALTER COLUMN created_at SET DEFAULT now();

ALTER TABLE public.labels
    ALTER COLUMN created_at SET NOT NULL;
