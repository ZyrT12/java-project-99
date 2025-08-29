package hexlet.code.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (HttpMethod.GET.matches(method) && "/api/login".equals(uri)) {
            return true;
        }
        if (HttpMethod.OPTIONS.matches(method)) {
            return true;
        }
        if ("/".equals(uri) || "/api".equals(uri) || "/index.html".equals(uri)
                || uri.startsWith("/assets/") || "/favicon.ico".equals(uri)) {
            return true;
        }
        if (HttpMethod.POST.matches(method) && "/api/login".equals(uri)) {
            return true;
        }
        if (HttpMethod.POST.matches(method) && "/api/users".equals(uri)) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(AUTH_HEADER);
        if (header == null || !header.regionMatches(true, 0, BEARER, 0, BEARER.length())) {
            chain.doFilter(request, response);
            return;
        }
        try {
            String token = header.substring(BEARER.length()).trim();
            Optional<DecodedJWT> verified = jwtService.verify(token);
            if (verified.isPresent()) {
                DecodedJWT jwt = verified.get();
                String subject = jwt.getSubject();
                if (subject != null && !subject.isEmpty()) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(subject,
                            null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }
}
