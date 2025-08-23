package hexlet.code.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalAuthAdviceTest {

    @Test
    void handleBodyErrorsOnLoginReturns401() throws Exception {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/api/login");
        Exception ex = new HttpMessageNotReadableException("x");
        ResponseEntity<Map<String, String>> resp = advice.handleBodyErrors(ex, req);
        assertEquals(401, resp.getStatusCode().value());
    }

    @Test
    void handleBodyErrorsOnOtherReturns400() throws Exception {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/api/tasks");
        Exception ex = Mockito.mock(MethodArgumentNotValidException.class);
        ResponseEntity<Map<String, String>> resp = advice.handleBodyErrors(ex, req);
        assertEquals(400, resp.getStatusCode().value());
    }

    @Test
    void handleAnyOnLoginReturns401() {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/login");
        Exception ex = new RuntimeException("x");
        ResponseEntity<Map<String, String>> resp = advice.handleAny(ex, req);
        assertEquals(401, resp.getStatusCode().value());
    }

    @Test
    void handleAnyOnOtherReturns500() {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/api/labels");
        Exception ex = new RuntimeException("x");
        ResponseEntity<Map<String, String>> resp = advice.handleAny(ex, req);
        assertEquals(500, resp.getStatusCode().value());
    }
}
