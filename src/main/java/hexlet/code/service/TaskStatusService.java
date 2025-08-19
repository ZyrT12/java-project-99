package hexlet.code.service;

import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.model.TaskStatus;

import java.util.List;

public interface TaskStatusService {
    List<TaskStatus> findAll();
    TaskStatus findById(Long id);
    TaskStatus create(TaskStatusUpsertDto dto);
    TaskStatus updatePartial(Long id, TaskStatusUpsertDto dto);
    void delete(Long id);
}
