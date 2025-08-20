package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public abstract class TaskBaseDto {
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("status")
    private String status;

    private Long assigneeId;
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

    @JsonProperty("assignee_id")
    public Long getAssigneeId() {
        return assigneeId;
    }

    @JsonProperty("taskLabelIds")
    public List<Long> getTaskLabelIds() {
        return taskLabelIds;
    }
    @JsonProperty("taskStatusId")
    public Long getTaskStatusId() {
        return taskStatusId;
    }

    @JsonProperty("executorId")
    public Long getExecutorId() {
        return executorId;
    }

    @JsonProperty("labelIds")
    public List<Long> getLabelIds() {
        return labelIds;
    }

    public String getDescription() {
        return description != null ? description : content;
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

    @JsonProperty("assignee_id")
    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
        if (this.executorId == null) {
            this.executorId = assigneeId;
        }
    }

    @JsonProperty("taskLabelIds")
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

    @JsonProperty("taskStatusId")
    public void setTaskStatusId(Long taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    @JsonProperty("executorId")
    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
        if (this.assigneeId == null) {
            this.assigneeId = executorId;
        }
    }

    @JsonProperty("labelIds")
    public void setLabelIds(List<Long> labelIds) {
        this.labelIds = labelIds;
        if (this.taskLabelIds == null) {
            this.taskLabelIds = labelIds;
        }
    }
}
