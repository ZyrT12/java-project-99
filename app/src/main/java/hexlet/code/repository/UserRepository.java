package hexlet.code.repository;

import hexlet.code.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public User save(User u) {
        if (u.getId() == null) {
            em.persist(u);
            return u;
        }
        return em.merge(u);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public Optional<User> findByEmail(String email) {
        var list = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return list.stream().findFirst();
    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
    }

    public void delete(User u) {
        em.remove(em.contains(u) ? u : em.merge(u));
    }
}
