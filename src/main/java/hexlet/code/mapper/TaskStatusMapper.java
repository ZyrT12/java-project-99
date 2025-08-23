package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskStatusResponseDto;
import hexlet.code.model.TaskStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskStatusMapper {
    TaskStatusResponseDto toDto(TaskStatus status);
}
