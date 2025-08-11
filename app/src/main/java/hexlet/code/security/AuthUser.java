package hexlet.code.security;

public record AuthUser(long id, String email, String role) {
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}
