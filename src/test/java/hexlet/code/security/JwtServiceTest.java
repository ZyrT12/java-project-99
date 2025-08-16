package hexlet.code.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    @Test
    void issueAndVerify() {
        JwtService service = new JwtService("test-secret");
        String token = service.issue(10L, "jack@example.com");

        Optional<DecodedJWT> decoded = service.verify(token);
        assertTrue(decoded.isPresent());

        DecodedJWT jwt = decoded.get();
        assertEquals(10L, jwt.getClaim("uid").asLong());
        assertEquals("jack@example.com", jwt.getSubject());
    }
}
