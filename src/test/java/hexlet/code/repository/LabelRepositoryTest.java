package hexlet.code.repository;

import hexlet.code.model.Label;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LabelRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;

    @Test
    void saveAndFindByNameIgnoreCase() {
        Label l = new Label();
        l.setName("Bug");
        l.setSlug("bug");
        Label saved = labelRepository.save(l);

        Optional<Label> byName = labelRepository.findByNameIgnoreCase("BUG");
        assertThat(byName).isPresent();
        assertThat(byName.get().getId()).isEqualTo(saved.getId());
        assertThat(labelRepository.existsByNameIgnoreCase("bug")).isTrue();
    }
}
