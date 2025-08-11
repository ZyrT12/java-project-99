package hexlet.code.config;

public final class SecurityConfig {

    public static final String JWT_SECRET = System.getenv().getOrDefault("JWT_SECRET", "change-me-in-prod");
    public static final long JWT_TTL_SECONDS = 60L * 60L * 24L; // 24 часа
    private SecurityConfig() {}
}