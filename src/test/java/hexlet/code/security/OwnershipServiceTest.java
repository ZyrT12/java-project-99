package hexlet.code.security;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnershipServiceTest {

    @Mock
    private UserRepository userRepository;

    private OwnershipService service;

    @BeforeEach
    void setUp() {
        service = new OwnershipService(userRepository);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void returnsFalseWhenNoAuthentication() {
        SecurityContextHolder.clearContext();
        boolean result = service.isSelf(1L);
        assertThat(result).isFalse();
    }

    @Test
    void returnsFalseWhenAuthNameNull() {
        SecurityContext ctx = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(null);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        boolean result = service.isSelf(1L);
        assertThat(result).isFalse();
    }

    @Test
    void returnsFalseWhenUserNotFound() {
        SecurityContext ctx = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("john@example.com");
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = service.isSelf(1L);
        assertThat(result).isFalse();
    }

    @Test
    void returnsTrueWhenEmailMatchesIgnoringCase() {
        SecurityContext ctx = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("John@Example.com");
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        User u = new User();
        u.setEmail("john@example.com");
        when(userRepository.findById(7L)).thenReturn(Optional.of(u));

        boolean result = service.isSelf(7L);
        assertThat(result).isTrue();
    }

    @Test
    void returnsFalseWhenEmailDoesNotMatch() {
        SecurityContext ctx = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("other@example.com");
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        User u = new User();
        u.setEmail("john@example.com");
        when(userRepository.findById(7L)).thenReturn(Optional.of(u));

        boolean result = service.isSelf(7L);
        assertThat(result).isFalse();
    }
}
