ALTER TABLE public.users
ALTER COLUMN created_at TYPE timestamptz
        USING (created_at::timestamp AT TIME ZONE 'UTC');
ALTER TABLE public.users
    ALTER COLUMN created_at SET DEFAULT now();
ALTER TABLE public.users
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE public.task_statuses
ALTER COLUMN created_at TYPE timestamptz
        USING (created_at::timestamp AT TIME ZONE 'UTC');
ALTER TABLE public.task_statuses
    ALTER COLUMN created_at SET DEFAULT now();
ALTER TABLE public.task_statuses
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE public.tasks
ALTER COLUMN created_at TYPE timestamptz
        USING (created_at::timestamp AT TIME ZONE 'UTC');
ALTER TABLE public.tasks
    ALTER COLUMN created_at SET DEFAULT now();
ALTER TABLE public.tasks
    ALTER COLUMN created_at SET NOT NULL;
