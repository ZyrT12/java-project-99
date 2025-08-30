package hexlet.code.mapper;

import hexlet.code.dto.tasks.TaskResponseDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskMapperTest {

    private TaskMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TaskMapper.class);
    }

    @Test
    void updateFromDtoSetsAssigneeAndLabels() {
        TaskMappingHelper helper = Mockito.mock(TaskMappingHelper.class);

        User u = new User();
        u.setId(10L);
        Mockito.when(helper.userById(10L)).thenReturn(u);

        Label l1 = new Label();
        l1.setId(1L);
        l1.setName("L1");
        Label l2 = new Label();
        l2.setId(2L);
        l2.setName("L2");
        Set<Label> labels = new HashSet<>(List.of(l1, l2));
        Mockito.when(helper.labelsByIds(Mockito.anyList())).thenReturn(labels);

        TaskUpsertDto dto = new TaskUpsertDto();
        dto.setTitle("T");
        dto.setContent("C");
        dto.setAssigneeId(10L);
        dto.setLabelIds(List.of(1L, 2L));

        Task task = new Task();
        mapper.updateFromDto(dto, task, helper);

        assertEquals("T", task.getTitle());
        assertEquals("C", task.getContent());
        assertNotNull(task.getAssignee());
        assertEquals(10L, task.getAssignee().getId());
        assertNotNull(task.getLabels());
        assertEquals(2, task.getLabels().size());
    }

    @Test
    void toResponseMapsIds() {
        User u = new User();
        u.setId(10L);

        Label l1 = new Label();
        l1.setId(1L);
        l1.setName("L1");
        Label l2 = new Label();
        l2.setId(2L);
        l2.setName("L2");

        Task t = new Task();
        t.setCreatedAt(LocalDate.now());
        t.setTitle("A");
        t.setContent("B");
        t.setAssignee(u);
        t.setLabels(new HashSet<>(List.of(l1, l2)));

        TaskResponseDto dto = mapper.toResponse(t);

        assertEquals("A", dto.getTitle());
        assertEquals("B", dto.getContent());
        assertEquals(10L, dto.getAssigneeId());
        List<Long> ids = dto.getLabelIds();
        assertNotNull(ids);
        assertEquals(2, ids.size());
        assertTrue(ids.containsAll(List.of(1L, 2L)));
    }
}
