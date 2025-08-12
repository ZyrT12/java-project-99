package hexlet.code.dto.labels;

import java.time.LocalDate;

public class LabelDto {
    private Long id;
    private String name;
    private LocalDate createdAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
