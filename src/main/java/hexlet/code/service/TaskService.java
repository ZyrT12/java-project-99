package hexlet.code.service;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto get(Long id);
    List<TaskResponseDto> list();
    TaskResponseDto create(TaskUpsertDto dto);
    TaskResponseDto update(Long id, TaskUpsertDto dto);
    void delete(Long id);
    List<TaskResponseDto> list(String titleCont, Long assigneeId, String statusSlug, Long labelId);
}
