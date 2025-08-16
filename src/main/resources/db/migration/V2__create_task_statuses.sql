create table if not exists task_statuses (
                                             id          bigserial primary key,
                                             name        varchar(255) not null,
    slug        varchar(255) not null unique,
    created_at  date not null default current_date
    );

create index if not exists idx_task_statuses_slug on task_statuses(slug);
