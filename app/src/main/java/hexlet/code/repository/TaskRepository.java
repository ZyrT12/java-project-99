package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    @EntityGraph(attributePaths = {"taskStatus", "assignee"})
    List<Task> findAll();
}
