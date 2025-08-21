package hexlet.code.exception;

import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private static class DummyTarget {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String n) {
            this.name = n;
        }
    }

    @Test
    void onAuthReturns401() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        ResponseEntity<Void> r = h.onAuth(new AuthenticationException("bad") { });
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void onNotFoundReturns404() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        ResponseEntity<Void> r = h.onNotFound(new EntityNotFoundException("missing"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void onConflictReturns409() {
        GlobalExceptionHandler h = new GlobalExceptionHandler();
        ResponseEntity<Void> r = h.onConflict(new DataIntegrityViolationException("conflict"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void onValidationReturns422WithErrors() throws Exception {
        GlobalExceptionHandler h = new GlobalExceptionHandler();

        DummyTarget target = new DummyTarget();
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(target, "dummy");
        binding.addError(new FieldError("dummy", "name", "must not be blank"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, binding);

        ResponseEntity<Map<String, String>> r = h.onValidation(ex);
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(r.getBody()).containsKey("name");
        assertThat(r.getBody().get("name")).contains("must not be blank");
    }
}
