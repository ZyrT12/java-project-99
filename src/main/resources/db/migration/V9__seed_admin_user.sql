UPDATE users
SET password_hash = '$2a$10$IWhfY8nJe4DmN6I7TC1TFew6G96BzegPXNw3u35hDKbZ7cmPUhdze',
    first_name    = 'Admin',
    last_name     = 'User'
WHERE email = 'admin@local';

INSERT INTO users (email, password_hash, first_name, last_name, created_at)
SELECT 'admin@local',
       '$2a$10$IWhfY8nJe4DmN6I7TC1TFew6G96BzegPXNw3u35hDKbZ7cmPUhdze',
       'Admin',
       'User',
       CURRENT_DATE
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@local');
