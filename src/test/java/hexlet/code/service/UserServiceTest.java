package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceTest {

    private UserRepository repo;
    private UserService service;

    @BeforeEach
    void setUp() {
        repo = mock(UserRepository.class);
        service = new UserService(repo);
    }

    private static void setId(User user, Long id) {
        try {
            Field f = User.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(user, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createOk() {
        UserCreateDto dto = new UserCreateDto("test@example.com", "John", "Doe", "pass123");
        when(repo.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserResponseDto result = service.create(dto);

        assertEquals("test@example.com", result.email());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repo).save(captor.capture());
        assertTrue(captor.getValue().getPasswordHash().startsWith("$2"));
    }

    @Test
    void createRejectsDuplicateEmail() {
        UserCreateDto dto = new UserCreateDto("exists@example.com", "A", "B", "pass123");
        when(repo.findByEmail(dto.email())).thenReturn(Optional.of(new User()));
        assertThrows(IllegalArgumentException.class, () -> service.create(dto));
    }

    @Test
    void getOk() {
        User u = new User();
        setId(u, 1L);
        u.setEmail("x@x.com");
        when(repo.findById(1L)).thenReturn(Optional.of(u));

        UserResponseDto dto = service.get(1L);
        assertEquals("x@x.com", dto.email());
    }

    @Test
    void getNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.get(99L));
    }

    @Test
    void listOk() {
        User u1 = new User();
        u1.setEmail("a@a.com");
        when(repo.findAll()).thenReturn(List.of(u1));

        List<UserResponseDto> list = service.list();
        assertEquals(1, list.size());
        assertEquals("a@a.com", list.get(0).email());
    }

    @Test
    void updateOk() {
        User u = new User();
        setId(u, 1L);
        u.setEmail("old@mail.com");
        when(repo.findById(1L)).thenReturn(Optional.of(u));
        when(repo.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserUpdateDto dto = new UserUpdateDto("new@mail.com", "F", "L", "newpass");
        UserResponseDto result = service.update(1L, dto);
        assertEquals("new@mail.com", result.email());
    }

    @Test
    void updateShortPasswordRejected() {
        User u = new User();
        setId(u, 1L);
        when(repo.findById(1L)).thenReturn(Optional.of(u));
        UserUpdateDto dto = new UserUpdateDto(null, null, null, "12");
        assertThrows(IllegalArgumentException.class, () -> service.update(1L, dto));
    }

    @Test
    void deleteOk() {
        User u = new User();
        setId(u, 5L);
        when(repo.findById(5L)).thenReturn(Optional.of(u));

        service.delete(5L);
        verify(repo).delete(u);
    }
}
