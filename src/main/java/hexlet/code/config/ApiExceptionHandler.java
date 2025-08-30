package hexlet.code.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuth(AuthenticationException e) {
        String message = Optional.ofNullable(e.getMessage()).orElse("Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", message));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleRse(ResponseStatusException e) {
        String message = Optional.ofNullable(e.getReason()).orElse("Error");
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", message));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEnf(EntityNotFoundException e) {
        String message = Optional.ofNullable(e.getMessage()).orElse("Not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuch(NoSuchElementException e) {
        String message = Optional.ofNullable(e.getMessage()).orElse("Not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleManve(MethodArgumentNotValidException e, HttpServletRequest req) {
        if ("/api/login".equals(req.getRequestURI())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), Optional.ofNullable(fe.getDefaultMessage()).orElse("Invalid"));
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleCve(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("error", "Invalid"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception e) {
        String message = Optional.ofNullable(e.getMessage()).orElse("Unexpected error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", message));
    }
}
