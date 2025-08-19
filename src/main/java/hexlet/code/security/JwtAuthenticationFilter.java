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
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader(AUTH_HEADER);
            if (header != null && startsWithBearer(header)) {
                String token = header.substring(BEARER_PREFIX.length()).trim();
                Optional<DecodedJWT> decoded = jwtService.verify(token);
                if (decoded.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                    String subject = decoded.get().getSubject();
                    Authentication auth = new UsernamePasswordAuthenticationToken(subject, null,
                            Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ignored) {
        }
        filterChain.doFilter(request, response);
    }

    private boolean startsWithBearer(String header) {
        if (header.length() < BEARER_PREFIX.length()) {
            return false;
        }
        String prefix = header.substring(0, BEARER_PREFIX.length());
        return prefix.toLowerCase(Locale.ROOT).equals("bearer ");
    }
}
