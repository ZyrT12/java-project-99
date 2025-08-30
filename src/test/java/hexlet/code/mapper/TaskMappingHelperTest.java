package hexlet.code.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TaskMappingHelperTest {

    @Autowired
    private TaskMappingHelper helper;

    @Test
    void beanExists() {
        assertThat(helper).isNotNull();
    }
}
