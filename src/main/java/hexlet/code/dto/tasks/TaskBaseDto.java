package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public abstract class TaskBaseDto {
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("status")
    private String status;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    @JsonProperty("taskLabelIds")
    private List<Long> taskLabelIds;

    private String description;
    private Long taskStatusId;
    private Long executorId;
    private List<Long> labelIds;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public List<Long> getTaskLabelIds() {
        return taskLabelIds;
    }

    public String getDescription() {
        return description != null ? description : content;
    }

    public Long getTaskStatusId() {
        return taskStatusId;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public List<Long> getLabelIds() {
        return labelIds;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
        if (this.description == null) {
            this.description = content;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
        if (this.executorId == null) {
            this.executorId = assigneeId;
        }
    }

    public void setTaskLabelIds(List<Long> taskLabelIds) {
        this.taskLabelIds = taskLabelIds;
        if (this.labelIds == null) {
            this.labelIds = taskLabelIds;
        }
    }

    public void setDescription(String description) {
        this.description = description;
        if (this.content == null) {
            this.content = description;
        }
    }

    public void setTaskStatusId(Long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
        if (this.assigneeId == null) {
            this.assigneeId = executorId;
        }
    }

    public void setLabelIds(List<Long> labelIds) {
        this.labelIds = labelIds;
        if (this.taskLabelIds == null) {
            this.taskLabelIds = labelIds;
        }
    }
}
