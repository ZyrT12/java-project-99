package hexlet.code.dto.tasks;

import java.time.LocalDate;
import java.util.List;

public class TaskResponseDto {
    private Long id;
    private Integer index;
    private LocalDate createdAt;
    private String title;
    private String description;
    private Long executorId;
    private Long taskStatusId;
    private List<Long> labelIds;

    public Long getId() {
        return id;
    }

    public Integer getIndex() {
        return index;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
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
