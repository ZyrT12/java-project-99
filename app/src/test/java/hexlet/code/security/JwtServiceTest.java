package hexlet.code.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class JwtServiceTest {

    @Test
    void createAndVerifyToken() {
        String token = JwtService.createToken(10L, "jack@example.com", "USER");
        Optional<DecodedJWT> decoded = JwtService.verify(token);
        assertTrue(decoded.isPresent());
        DecodedJWT jwt = decoded.get();
        assertEquals(10L, jwt.getClaim("uid").asLong());
        assertEquals("jack@example.com", jwt.getClaim("email").asString());
        assertEquals("USER", jwt.getClaim("role").asString());
    }

    @Test
    void verifyInvalidTokenReturnsEmpty() {
        Optional<DecodedJWT> decoded = JwtService.verify("invalid.token.string");
        assertFalse(decoded.isPresent());
    }
}
