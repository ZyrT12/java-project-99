package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "index", source = "index")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "status",
            expression = "java(task.getTaskStatus() != null ? task.getTaskStatus().getSlug() : null)")
    @Mapping(target = "assigneeId",
            expression = "java(task.getAssignee() != null ? task.getAssignee().getId() : null)")
    @Mapping(target = "taskLabelIds", source = "task", qualifiedByName = "mapLabelIds")
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "taskStatusId", ignore = true)
    @Mapping(target = "executorId", ignore = true)
    @Mapping(target = "labelIds", ignore = true)
    public abstract TaskResponseDto toResponse(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "index", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract void updateFromDto(TaskUpsertDto dto,
                                       @MappingTarget Task task,
                                       @Context TaskMappingHelper helper);

    @AfterMapping
    protected void fillRefs(TaskUpsertDto dto,
                            @MappingTarget Task task,
                            @Context TaskMappingHelper helper) {
        if (dto == null) {
            return;
        }
        if (dto.getTaskStatusId() == null && dto.getStatus() != null && !dto.getStatus().isBlank()) {
            task.setTaskStatus(helper.statusByValue(dto.getStatus()));
        }
        Long aid = dto.getAssigneeId() != null ? dto.getAssigneeId() : dto.getExecutorId();
        if (aid != null) {
            if (aid == 0L) {
                task.setAssignee(null);
            } else {
                task.setAssignee(helper.userById(aid));
            }
        }
        List<Long> ids = dto.getLabelIds() != null ? dto.getLabelIds() : dto.getTaskLabelIds();
        if (ids != null) {
            task.setLabels(helper.labelsByIds(ids));
        }
    }

    @Named("mapLabelIds")
    protected static List<Long> mapLabelIds(Task task) {
        if (task.getLabels() == null) {
            return new ArrayList<>();
        }
        return task.getLabels().stream().map(Label::getId).collect(Collectors.toList());
    }
}
