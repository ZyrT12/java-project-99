package hexlet.code.config;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskStatusInitializer {

    @Bean
    ApplicationRunner loadDefaults(TaskStatusRepository repo) {
        return args -> {
            List<TaskStatus> defaults = List.of(
                    new TaskStatus("Draft", "draft"),
                    new TaskStatus("ToReview", "to_review"),
                    new TaskStatus("ToBeFixed", "to_be_fixed"),
                    new TaskStatus("ToPublish", "to_publish"),
                    new TaskStatus("Published", "published")
            );
            for (TaskStatus s : defaults) {
                if (!repo.existsBySlugIgnoreCase(s.getSlug())) {
                    repo.save(new TaskStatus(s.getName(), s.getSlug()));
                }
            }
        };
    }
}
