package hexlet.code.repository;

import hexlet.code.model.TaskStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskStatusRepositoryTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    void saveAndFindBySlug() {
        TaskStatus s = new TaskStatus();
        s.setName("In Progress");
        s.setSlug("in_progress");
        TaskStatus saved = taskStatusRepository.save(s);

        Optional<TaskStatus> found = taskStatusRepository.findBySlug("in_progress");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());

        Optional<TaskStatus> missing = taskStatusRepository.findBySlug("absent");
        assertThat(missing).isEmpty();
    }
}
