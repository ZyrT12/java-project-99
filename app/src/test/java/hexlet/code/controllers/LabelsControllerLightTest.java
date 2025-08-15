package hexlet.code.controllers;

import hexlet.code.dto.labels.LabelDto;
import hexlet.code.service.LabelService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class LabelsControllerLightTest {

    @Test
    void updateDelegatesToService() {
        LabelService service = Mockito.mock(LabelService.class);
        LabelsController controller = new LabelsController(service);
        Mockito.when(service.update(eq(1L), any())).thenReturn(new LabelDto());
        assertDoesNotThrow(() -> controller.update(1L, null));
        Mockito.verify(service, Mockito.times(1)).update(eq(1L), any());
    }

    @Test
    void getAllSetsTotalHeaderAndPagination() {
        LabelService service = Mockito.mock(LabelService.class);
        LabelsController controller = new LabelsController(service);

        LabelDto dto1 = new LabelDto();
        LabelDto dto2 = new LabelDto();
        Mockito.when(service.getAll()).thenReturn(List.of(dto1, dto2));

        ResponseEntity<List<LabelDto>> resp = controller.getAll(0, 1);

        assertThat(resp.getHeaders().getFirst("X-Total-Count")).isEqualTo("2");
        assertThat(resp.getBody()).containsExactly(dto1);
    }
}
