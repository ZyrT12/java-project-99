package hexlet.code.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

public final class JwtService {
    private static final String SECRET = System.getenv().getOrDefault("JWT_SECRET", "change-me-in-prod");
    private static final Algorithm ALG = Algorithm.HMAC256(SECRET);
    private static final long TTL_SECONDS = 60L * 60L * 24L; // 24 часа

    public static String createToken(Long userId, String email, String role) {
        var now = Instant.now();
        return JWT.create()
                .withIssuer("app")
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusSeconds(TTL_SECONDS)))
                .withClaim("uid", userId)
                .withClaim("email", email)
                .withClaim("role", role)
                .sign(ALG);
    }

    public static Optional<DecodedJWT> verify(String token) {
        try {
            var verifier = JWT.require(ALG).withIssuer("app").build();
            return Optional.of(verifier.verify(token));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private JwtService() {

    }
}
