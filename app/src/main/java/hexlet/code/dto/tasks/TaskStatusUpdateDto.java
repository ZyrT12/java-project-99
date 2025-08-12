package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskStatusUpdateDto {
    private String name;
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

