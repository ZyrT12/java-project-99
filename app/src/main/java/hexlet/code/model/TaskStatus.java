package hexlet.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.PrePersist;
import java.time.LocalDate;

@Entity
@Table(
        name = "task_statuses",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_task_status_name", columnNames = "name"),
            @UniqueConstraint(name = "uk_task_status_slug", columnNames = "slug")
        }
)
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1)
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Size(min = 1)
    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }

    public TaskStatus() {
    }

    public TaskStatus(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
