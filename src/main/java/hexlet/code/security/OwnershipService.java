package hexlet.code.security;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("ownershipService")
public class OwnershipService {
    private final UserRepository userRepository;

    public OwnershipService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isSelf(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return false;
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }
        String emailFromToken = auth.getName();
        return emailFromToken.equalsIgnoreCase(userOpt.get().getEmail());
    }
}
