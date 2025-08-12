package hexlet.code.repository;

import hexlet.code.model.Label;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
