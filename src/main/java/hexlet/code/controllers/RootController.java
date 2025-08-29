package hexlet.code.controllers;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping({"/", "/api"})
    public ResponseEntity<Map<String, String>> index() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
