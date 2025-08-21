package hexlet.code.config;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskStatusInitializerTest {

    @Test
    void savesDefaultsWhenEmpty() throws Exception {
        TaskStatusRepository repo = mock(TaskStatusRepository.class);
        when(repo.findBySlug(anyString())).thenReturn(Optional.empty());

        TaskStatusInitializer init = new TaskStatusInitializer();
        ApplicationRunner runner = init.loadDefaults(repo);

        assertThatCode(() -> runner.run(new DefaultApplicationArguments(new String[]{}))).doesNotThrowAnyException();
        verify(repo, times(5)).save(any(TaskStatus.class));
    }
}
