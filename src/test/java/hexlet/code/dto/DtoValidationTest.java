package hexlet.code.dto;

import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import hexlet.code.dto.tasks.TaskUpsertDto;
import hexlet.code.dto.tasks.OnCreate;
import hexlet.code.dto.tasks.OnUpdate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void taskStatusUpsertDtoCreateGroup() {
        TaskStatusUpsertDto dto = new TaskStatusUpsertDto();
        dto.setName("");
        dto.setSlug("");
        Set<ConstraintViolation<TaskStatusUpsertDto>> violations = validator.validate(dto, OnCreate.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void taskStatusUpsertDtoUpdateGroup() {
        TaskStatusUpsertDto dto = new TaskStatusUpsertDto();
        dto.setName("Ok");
        dto.setSlug("ok");
        Set<ConstraintViolation<TaskStatusUpsertDto>> violations = validator.validate(dto, OnUpdate.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void taskUpsertDtoCreateGroup() {
        TaskUpsertDto dto = new TaskUpsertDto();
        Set<ConstraintViolation<TaskUpsertDto>> violations = validator.validate(dto, OnCreate.class);
        assertFalse(violations.isEmpty());
    }
}
