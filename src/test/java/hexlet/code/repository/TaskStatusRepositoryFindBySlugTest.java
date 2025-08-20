package hexlet.code.repository;

import hexlet.code.model.TaskStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskStatusRepositoryFindBySlugTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    void findBySlugPresent() {
        TaskStatus ts = new TaskStatus();
        ts.setName("In progress");
        ts.setSlug("in_progress");
        taskStatusRepository.save(ts);

        Optional<TaskStatus> found = taskStatusRepository.findBySlug("in_progress");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("In progress");
    }

    @Test
    void findBySlugAbsent() {
        Optional<TaskStatus> found = taskStatusRepository.findBySlug("missing_slug");
        assertThat(found).isEmpty();
    }
}
