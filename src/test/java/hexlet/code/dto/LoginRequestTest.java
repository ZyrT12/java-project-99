package hexlet.code.dto;

import hexlet.code.dto.auth.LoginRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    private Validator validator() {
        ValidatorFactory f = Validation.buildDefaultValidatorFactory();
        return f.getValidator();
    }

    @Test
    void beanValidationWorks() {
        Validator v = validator();
        LoginRequest bad = new LoginRequest();
        Set<ConstraintViolation<LoginRequest>> violations = v.validate(bad);
        assertFalse(violations.isEmpty());
        LoginRequest ok = new LoginRequest();
        ok.setEmail("u@e.com");
        ok.setPassword("secret");
        assertTrue(v.validate(ok).isEmpty());
        assertEquals("u@e.com", ok.getEmail());
        assertEquals("secret", ok.getPassword());
    }
}
