package hexlet.code.dto;

import java.util.List;

import hexlet.code.dto.tasks.TaskResponseDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskResponseDtoTest {

    @Test
    void gettersAndSetters() {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(7L);
        dto.setIndex(3);
        dto.setAssigneeId(42L);
        dto.setTaskLabelIds(List.of(1L, 2L, 3L));
        dto.setLabelIds(List.of(5L));

        assertThat(dto.getId()).isEqualTo(7L);
        assertThat(dto.getIndex()).isEqualTo(3);
        assertThat(dto.getAssigneeId()).isEqualTo(42L);
        assertThat(dto.getTaskLabelIds()).containsExactly(1L, 2L, 3L);
        assertThat(dto.getLabelIds()).containsExactly(5L);
    }
}
