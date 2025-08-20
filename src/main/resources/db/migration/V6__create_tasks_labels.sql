CREATE TABLE IF NOT EXISTS tasks_labels (
                                            task_id  BIGINT NOT NULL REFERENCES tasks(id)  ON DELETE CASCADE,
    label_id BIGINT NOT NULL REFERENCES labels(id) ON DELETE RESTRICT,
    PRIMARY KEY (task_id, label_id)
    );

CREATE INDEX IF NOT EXISTS idx_tasks_labels_task  ON tasks_labels(task_id);
CREATE INDEX IF NOT EXISTS idx_tasks_labels_label ON tasks_labels(label_id);
