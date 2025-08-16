create table if not exists users (
                                     id          bigserial primary key,
                                     email       varchar(255) not null unique,
    password    varchar(255) not null,
    first_name  varchar(255),
    last_name   varchar(255),
    created_at  date not null default current_date
    );

create index if not exists idx_users_email on users(email);
