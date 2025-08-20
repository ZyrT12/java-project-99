package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public class TaskResponseDto extends TaskBaseDto {
    private Long id;
    private Integer index;
    private LocalDate createdAt;

    @JsonProperty("assignee_id")
    @Override
    public Long getAssigneeId() {
        return super.getAssigneeId();
    }

    @JsonProperty("taskLabelIds")
    @Override
    public List<Long> getTaskLabelIds() {
        return super.getTaskLabelIds();
    }

    @JsonIgnore
    @Override
    public Long getExecutorId() {
        return super.getExecutorId();
    }

    @JsonIgnore
    @Override
    public Long getTaskStatusId() {
        return super.getTaskStatusId();
    }

    @JsonIgnore
    @Override
    public List<Long> getLabelIds() {
        return super.getLabelIds();
    }

    @JsonIgnore
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    public Long getId() {
        return id;
    }

    public Integer getIndex() {
        return index;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
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
}
