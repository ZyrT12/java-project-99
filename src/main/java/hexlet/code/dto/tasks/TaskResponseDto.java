package hexlet.code.dto.tasks;

import java.time.LocalDate;

public class TaskResponseDto extends TaskBaseDto {
    private Long id;
    private Integer index;
    private LocalDate createdAt;

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
