package hexlet.code.repository;

import hexlet.code.model.Label;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LabelRepositoryCaseInsensitiveTest {

    @Autowired
    private LabelRepository labelRepository;

    @Test
    void findByNameIgnoreCaseWorks() {
        Label l = new Label();
        l.setName("Backend");
        labelRepository.save(l);

        Optional<Label> found = labelRepository.findByNameIgnoreCase("backend");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Backend");
    }

    @Test
    void existsByNameIgnoreCaseWorks() {
        Label l = new Label();
        l.setName("Docs");
        labelRepository.save(l);

        boolean existsLower = labelRepository.existsByNameIgnoreCase("docs");
        boolean existsUpper = labelRepository.existsByNameIgnoreCase("DOCS");

        assertThat(existsLower).isTrue();
        assertThat(existsUpper).isTrue();
    }
}
