package hexlet.code.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthUserTest {

    @Test
    void isAdminTrueWhenRoleAdmin() {
        AuthUser user = new AuthUser(1L, "admin@example.com", "ADMIN");
        assertTrue(user.isAdmin());
    }

    @Test
    void isAdminFalseWhenRoleUser() {
        AuthUser user = new AuthUser(2L, "user@example.com", "USER");
        assertFalse(user.isAdmin());
    }
}
