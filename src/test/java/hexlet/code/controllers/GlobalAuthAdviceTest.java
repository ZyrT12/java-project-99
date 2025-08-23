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
    void handleBodyErrors_onLogin_returns401() throws Exception {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/api/login");
        Exception ex = new HttpMessageNotReadableException("x");
        ResponseEntity<Map<String, String>> resp = advice.handleBodyErrors(ex, req);
        assertEquals(401, resp.getStatusCode().value());
    }

    @Test
    void handleBodyErrors_onOther_returns400() throws Exception {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/api/tasks");
        Exception ex = Mockito.mock(MethodArgumentNotValidException.class);
        ResponseEntity<Map<String, String>> resp = advice.handleBodyErrors(ex, req);
        assertEquals(400, resp.getStatusCode().value());
    }

    @Test
    void handleAny_onLogin_returns401() {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/login");
        Exception ex = new RuntimeException("x");
        ResponseEntity<Map<String, String>> resp = advice.handleAny(ex, req);
        assertEquals(401, resp.getStatusCode().value());
    }

    @Test
    void handleAny_onOther_returns500() {
        GlobalAuthAdvice advice = new GlobalAuthAdvice();
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/api/labels");
        Exception ex = new RuntimeException("x");
        ResponseEntity<Map<String, String>> resp = advice.handleAny(ex, req);
        assertEquals(500, resp.getStatusCode().value());
    }
}
