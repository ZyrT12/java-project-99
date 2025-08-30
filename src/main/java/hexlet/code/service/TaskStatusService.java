package hexlet.code.service;

import hexlet.code.dto.tasks.TaskStatusResponseDto;
import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import java.util.List;

public interface TaskStatusService {
    List<TaskStatusResponseDto> findAll();
    TaskStatusResponseDto findById(Long id);
    TaskStatusResponseDto create(TaskStatusUpsertDto dto);
    TaskStatusResponseDto updatePartial(Long id, TaskStatusUpsertDto dto);
    void delete(Long id);
}
