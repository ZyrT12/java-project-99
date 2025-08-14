package hexlet.code.dto.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class TaskUpsertDto {
    @NotBlank(groups = OnCreate.class)
    @Size(min = 1, max = 100, groups = OnCreate.class)
    private String title;
    private String description;
    private Long executorId;
    private Long taskStatusId;
    private List<Long> labelIds;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public Long getTaskStatusId() {
        return taskStatusId;
    }

    public List<Long> getLabelIds() {
        return labelIds;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public void setTaskStatusId(Long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public void setLabelIds(List<Long> labelIds) {
        this.labelIds = labelIds;
    }
}
