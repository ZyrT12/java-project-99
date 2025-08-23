package hexlet.code.dto.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskStatusUpsertDto {

    @NotBlank(groups = {OnCreate.class})
    @Size(min = 2, max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(groups = {OnCreate.class})
    @Size(min = 2, max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String slug;

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
