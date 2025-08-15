package hexlet.code.controllers;

import hexlet.code.dto.labels.LabelCreateDto;
import hexlet.code.dto.labels.LabelDto;
import hexlet.code.dto.labels.LabelUpdateDto;
import hexlet.code.service.LabelService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class LabelsControllerUnitTest {

    @Test
    void getAllWithoutPagination() {
        LabelService service = Mockito.mock(LabelService.class);
        LabelsController controller = new LabelsController(service);
        Mockito.when(service.getAll()).thenReturn(List.of(new LabelDto(), new LabelDto(), new LabelDto()));
        ResponseEntity<java.util.List<LabelDto>> resp = controller.getAll(null, null);
        assertThat(resp.getHeaders().getFirst("X-Total-Count")).isEqualTo("3");
        assertThat(resp.getBody()).hasSize(3);
    }

    @Test
    void getAllWithPagination() {
        LabelService service = Mockito.mock(LabelService.class);
        LabelsController controller = new LabelsController(service);
        LabelDto a = new LabelDto();
        LabelDto b = new LabelDto();
        LabelDto c = new LabelDto();
        Mockito.when(service.getAll()).thenReturn(List.of(a, b, c));
        ResponseEntity<java.util.List<LabelDto>> resp = controller.getAll(0, 2);
        assertThat(resp.getHeaders().getFirst("X-Total-Count")).isEqualTo("3");
        assertThat(resp.getBody()).containsExactly(a, b);
    }

    @Test
    void createDelegatesAndReturnsDto() {
        LabelService service = Mockito.mock(LabelService.class);
        LabelsController controller = new LabelsController(service);
        LabelDto created = new LabelDto();
        Mockito.when(service.create(any(LabelCreateDto.class))).thenReturn(created);
        LabelDto result = controller.create(new LabelCreateDto());
        assertThat(result).isSameAs(created);
        Mockito.verify(service, Mockito.times(1)).create(any(LabelCreateDto.class));
    }

    @Test
    void updateDelegatesAndReturnsDto() {
        LabelService service = Mockito.mock(LabelService.class);
        LabelsController controller = new LabelsController(service);
        LabelDto updated = new LabelDto();
        Mockito.when(service.update(eq(42L), any(LabelUpdateDto.class))).thenReturn(updated);
        LabelDto result = controller.update(42L, new LabelUpdateDto());
        assertThat(result).isSameAs(updated);
        Mockito.verify(service, Mockito.times(1)).update(eq(42L), any(LabelUpdateDto.class));
    }

    @Test
    void deleteDelegates() {
        LabelService service = Mockito.mock(LabelService.class);
        LabelsController controller = new LabelsController(service);
        controller.delete(7L);
        Mockito.verify(service, Mockito.times(1)).delete(7L);
    }
}
