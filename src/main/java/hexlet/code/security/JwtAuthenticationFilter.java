package hexlet.code.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";
    private final JwtService jwtService;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        String[] publicPatterns = new String[] {
            "/",
            "/index.html",
            "/favicon.ico",
            "/assets/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/static/**",
            "/actuator/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/login",
            "/api/users",
            "/api/users/**",
            "/api/tasks",
            "/api/tasks/**",
            "/api/task-statuses",
            "/api/task-statuses/**",
            "/api/task_statuses",
            "/api/task_statuses/**",
            "/api/labels",
            "/api/labels/**"
        };
        for (String pattern : publicPatterns) {
            if (matcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);
        if (header != null && startsWithBearer(header)) {
            String token = header.substring(BEARER_PREFIX.length()).trim();
            Optional<DecodedJWT> decoded = jwtService.verify(token);
            if (decoded.isPresent()) {
                String subject = decoded.get().getSubject();
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean startsWithBearer(String header) {
        if (header.length() < BEARER_PREFIX.length()) {
            return false;
        }
        String prefix = header.substring(0, BEARER_PREFIX.length());
        return prefix.toLowerCase(Locale.ROOT).equals("bearer");
    }
}
