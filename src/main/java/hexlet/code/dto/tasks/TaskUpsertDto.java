package hexlet.code.dto.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskUpsertDto extends TaskBaseDto {
    @Override
    @NotBlank(groups = OnCreate.class)
    @Size(min = 1, max = 100, groups = OnCreate.class)
    public String getTitle() {
        return super.getTitle();
    }
}
