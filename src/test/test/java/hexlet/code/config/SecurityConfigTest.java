package hexlet.code.config;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityConfigTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Test
    void publicEndpointsAreAccessible() {
        ResponseEntity<String> r1 = rest.getForEntity("http://localhost:" + port + "/api/task-statuses", String.class);
        ResponseEntity<String> r2 = rest.getForEntity("http://localhost:" + port + "/api/users", String.class);
        assertEquals(HttpStatus.OK, r1.getStatusCode());
        assertEquals(HttpStatus.OK, r2.getStatusCode());
    }

    @TestConfiguration
    static class SecurityTestConfig {
        @Bean
        public JwtDecoder jwtDecoder() {
            return token -> {
                Instant now = Instant.now();
                return Jwt.withTokenValue(token)
                        .headers(h -> h.putAll(Map.of("alg", "none")))
                        .claims(c -> c.putAll(Map.of("sub", "test")))
                        .issuedAt(now)
                        .expiresAt(now.plusSeconds(3600))
                        .build();
            };
        }
    }
}
