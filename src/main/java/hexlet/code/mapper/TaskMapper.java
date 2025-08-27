package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "index", ignore = true)
    Task toEntity(TaskUpsertDto dto);

    @Mapping(target = "status", source = "task.taskStatus.slug")
    @Mapping(target = "taskStatusId", source = "task.taskStatus.id")
    @Mapping(target = "executorId", source = "task.assignee.id")
    @Mapping(target = "assigneeId", source = "task.assignee.id")
    @Mapping(target = "labelIds", source = "task", qualifiedByName = "mapLabelIds")
    @Mapping(target = "taskLabelIds", source = "task", qualifiedByName = "mapLabelIds")
    @Mapping(target = "description", source = "task.content")
    TaskResponseDto toResponse(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "index", ignore = true)
    void updateFromDto(TaskUpsertDto dto, @MappingTarget Task task);

    @Named("mapLabelIds")
    static List<Long> mapLabelIds(Task task) {
        if (task.getLabels() == null) {
            return new ArrayList<>();
        }
        return task.getLabels().stream().map(Label::getId).collect(Collectors.toList());
    }
}
