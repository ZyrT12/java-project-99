insert into users (email, password_hash, first_name, last_name, created_at)
values (
           'admin@local',
           '$2a$10$IWhfY8nJe4DmN6I7TC1TFew6G96BzegPXNw3u35hDKbZ7cmPUhdze',
           'Admin',
           'User',
           current_date
       )
    on conflict (email) do update
                               set password_hash = excluded.password_hash,
                               first_name = excluded.first_name,
                               last_name = excluded.last_name;
