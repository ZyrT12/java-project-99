package hexlet.code.mapper;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class TaskMappingHelperExtendedTest {

    @Autowired
    private TaskMappingHelper helper;

    @Autowired
    private TaskStatusRepository statusRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LabelRepository labelRepo;

    @Autowired
    private TaskRepository taskRepo;

    @BeforeEach
    void clean() {
        taskRepo.deleteAll();
        labelRepo.deleteAll();
        statusRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void statusByValueOrDefaultReturnsDefaultOnNull() {
        TaskStatus def = new TaskStatus();
        def.setName("new");
        def.setSlug("new");
        statusRepo.save(def);

        TaskStatus result = helper.statusByValueOrDefault(null);
        assertThat(result.getSlug()).isEqualTo("new");
    }

    @Test
    void statusByValueOrDefaultReturnsDefaultOnBlank() {
        TaskStatus def = new TaskStatus();
        def.setName("new");
        def.setSlug("new");
        statusRepo.save(def);

        TaskStatus result = helper.statusByValueOrDefault("   ");
        assertThat(result.getName()).isEqualTo("new");
    }

    @Test
    void statusByValueOrDefaultFindsBySlugOrName() {
        TaskStatus s = new TaskStatus();
        s.setName("ToPublish");
        s.setSlug("to_publish");
        s = statusRepo.save(s);

        TaskStatus bySlug = helper.statusByValueOrDefault("to_publish");
        TaskStatus byName = helper.statusByValueOrDefault("ToPublish");

        assertThat(bySlug.getId()).isEqualTo(s.getId());
        assertThat(byName.getId()).isEqualTo(s.getId());
    }

    @Test
    void statusByValueReturnsNullOnNullOrBlank() {
        assertThat(helper.statusByValue(null)).isNull();
        assertThat(helper.statusByValue("")).isNull();
        assertThat(helper.statusByValue("   ")).isNull();
    }

    @Test
    void statusByValueFindsBySlugOrNameAndThrowsIfMissing() {
        TaskStatus s = new TaskStatus();
        s.setName("Draft");
        s.setSlug("draft");
        s = statusRepo.save(s);

        TaskStatus bySlug = helper.statusByValue("draft");
        TaskStatus byName = helper.statusByValue("Draft");
        assertThat(bySlug.getId()).isEqualTo(s.getId());
        assertThat(byName.getId()).isEqualTo(s.getId());

        assertThrows(java.util.NoSuchElementException.class, () -> helper.statusByValue("absent"));
    }

    @Test
    void userByIdReturnsNullOnNullAndEntityOnExistingId() {
        assertThat(helper.userById(null)).isNull();

        User u = new User();
        u.setEmail("u@example.com");
        u.setPasswordHash("pwd");
        u.setFirstName("A");
        u.setLastName("B");
        u = userRepo.save(u);

        User found = helper.userById(u.getId());
        assertThat(found.getId()).isEqualTo(u.getId());
        assertThat(found.getEmail()).isEqualTo("u@example.com");
    }

    @Test
    void labelsByIdsReturnsNullOnNullAndSetOnIds() {
        assertThat(helper.labelsByIds(null)).isNull();

        Label l1 = new Label();
        l1.setName("L1");
        l1 = labelRepo.save(l1);

        Label l2 = new Label();
        l2.setName("L2");
        l2 = labelRepo.save(l2);

        Set<Label> set = helper.labelsByIds(List.of(l1.getId(), l2.getId()));
        assertThat(set).hasSize(2);
        assertThat(set).
                extracting(Label::getName).containsExactlyInAnyOrder("L1", "L2");
    }
}
