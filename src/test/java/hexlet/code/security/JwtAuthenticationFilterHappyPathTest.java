package hexlet.code.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthenticationFilterHappyPathTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void withValidBearerTokenSetsAuthentication() throws Exception {
        JwtService jwtService = new JwtService("test-secret");
        String token = jwtService.issue(99L, "u@example.com");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        filter.doFilter(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.isAuthenticated()).isTrue();
        assertThat(auth.getName()).isEqualTo("u@example.com");
        Mockito.verify(chain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void withInvalidBearerTokenDoesNotAuthenticate() throws Exception {
        JwtService jwtService = new JwtService("test-secret");
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer invalid");

        filter.doFilter(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        Mockito.verify(chain, Mockito.times(1)).doFilter(request, response);
    }
}
