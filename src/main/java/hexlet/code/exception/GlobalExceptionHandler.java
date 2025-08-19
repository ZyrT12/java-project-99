package hexlet.code.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuth(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> onNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> onConflict(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> onValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (var e : ex.getBindingResult().getAllErrors()) {
            String field = e instanceof FieldError fe ? fe.getField() : e.getObjectName();
            errors.put(field, e.getDefaultMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(errors, headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
