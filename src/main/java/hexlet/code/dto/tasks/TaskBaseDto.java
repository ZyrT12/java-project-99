package hexlet.code.dto.tasks;

import java.util.List;

public abstract class TaskBaseDto {
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
