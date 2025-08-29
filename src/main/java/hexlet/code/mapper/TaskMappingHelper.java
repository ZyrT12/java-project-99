package hexlet.code.mapper;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class TaskMappingHelper {
    private final TaskStatusRepository statusRepo;
    private final UserRepository userRepo;
    private final LabelRepository labelRepo;

    public TaskMappingHelper(TaskStatusRepository statusRepo, UserRepository userRepo, LabelRepository labelRepo) {
        this.statusRepo = statusRepo;
        this.userRepo = userRepo;
        this.labelRepo = labelRepo;
    }

    public TaskStatus statusByValueOrDefault(String value) {
        if (value != null && !value.isBlank()) {
            Optional<TaskStatus> bySlug = statusRepo.findBySlug(value);
            Optional<TaskStatus> byName = statusRepo.findByNameIgnoreCase(value);
            return bySlug.orElseGet(() -> byName.orElseThrow(NoSuchElementException::new));
        }
        return statusRepo.findBySlug("new")
                .orElseGet(() -> statusRepo.findByNameIgnoreCase("new").orElseThrow(NoSuchElementException::new));
    }

    public TaskStatus statusByValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Optional<TaskStatus> bySlug = statusRepo.findBySlug(value);
        Optional<TaskStatus> byName = statusRepo.findByNameIgnoreCase(value);
        return bySlug.orElseGet(() -> byName.orElseThrow(NoSuchElementException::new));
    }

    public User userById(Long id) {
        if (id == null) {
            return null;
        }
        return userRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Set<Label> labelsByIds(List<Long> ids) {
        if (ids == null) {
            return null;
        }
        return new HashSet<>(labelRepo.findAllById(ids));
    }
}
