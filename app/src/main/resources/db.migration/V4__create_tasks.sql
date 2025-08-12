create table if not exists tasks (
    id           bigserial primary key,
    index_number integer,
    created_at   date not null default current_date,
    title        varchar(255) not null,
    content      text,
    status_id    bigint not null,
    assignee_id  bigint,

    constraint fk_tasks_statuses
    foreign key (status_id) references task_statuses(id) on delete restrict,

    constraint fk_tasks_users
    foreign key (assignee_id) references users(id) on delete restrict
    );

create index if not exists idx_tasks_status   on tasks(status_id);
create index if not exists idx_tasks_assignee on tasks(assignee_id);
