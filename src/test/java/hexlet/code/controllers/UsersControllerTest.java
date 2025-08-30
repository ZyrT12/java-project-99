package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.users.UserCreateDto;
import hexlet.code.dto.users.UserResponseDto;
import hexlet.code.dto.users.UserUpdateDto;
import hexlet.code.security.JwtService;
import hexlet.code.security.OwnershipService;
import hexlet.code.service.UsersService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UsersControllerTest.Config.class)
public class UsersControllerTest {

    @TestConfiguration
    static class Config {
        @Bean
        UsersService usersService() {
            return Mockito.mock(UsersService.class);
        }

        @Bean
        OwnershipService ownershipService() {
            return Mockito.mock(OwnershipService.class);
        }

        @Bean
        JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersService usersService;

    @Autowired
    private OwnershipService ownershipService;

    @Test
    public void listUsers() throws Exception {
        UserResponseDto u1 = new UserResponseDto(
                1L,
                "a@example.com",
                "A",
                "B",
                LocalDate.now()
        );
        UserResponseDto u2 = new UserResponseDto(
                2L,
                "b@example.com",
                "C",
                "D",
                LocalDate.now()
        );
        Mockito.when(usersService.getAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].email").value("a@example.com")
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L));
    }

    @Test
    public void getUserById() throws Exception {
        UserResponseDto u = new UserResponseDto(
                5L,
                "x@example.com",
                "X",
                "Y",
                LocalDate.now()
        );
        Mockito.when(usersService.get(5L)).thenReturn(u);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/users/5")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(5L))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email").value("x@example.com")
            );
    }

    @Test
    public void createUser() throws Exception {
        UserCreateDto dto = new UserCreateDto(
                "new@example.com",
                "N",
                "M",
                "secret"
        );
        UserResponseDto resp = new UserResponseDto(
                10L,
                "new@example.com",
                "N",
                "M",
                LocalDate.now()
        );
        Mockito.when(usersService.create(Mockito.any(UserCreateDto.class))).thenReturn(resp);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(10L))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email").value("new@example.com")
            );
    }

    @Test
    public void replaceUser() throws Exception {
        Mockito.when(ownershipService.isSelf(7L)).thenReturn(true);
        UserUpdateDto dto = new UserUpdateDto(
                "upd@example.com",
                "U",
                "V",
                "pwd"
        );
        UserResponseDto resp = new UserResponseDto(
                7L,
                "upd@example.com",
                "U",
                "V",
                LocalDate.now()
        );
        Mockito.when(
                usersService.update(Mockito.eq(7L), Mockito.any(UserUpdateDto.class))
        ).thenReturn(resp);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/users/7")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(7L))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email").value("upd@example.com")
            );
    }

    @Test
    public void patchUser() throws Exception {
        Mockito.when(ownershipService.isSelf(8L)).thenReturn(true);
        UserUpdateDto dto = new UserUpdateDto(
                null,
                "P",
                null,
                null
        );
        UserResponseDto resp = new UserResponseDto(
                8L,
                "p8@example.com",
                "P",
                "Z",
                LocalDate.now()
        );
        Mockito.when(
                usersService.update(Mockito.eq(8L), Mockito.any(UserUpdateDto.class))
        ).thenReturn(resp);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/users/8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(8L))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.firstName").value("P")
            );
    }

    @Test
    public void deleteUser() throws Exception {
        Mockito.when(ownershipService.isSelf(12L)).thenReturn(true);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/users/12")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(usersService).delete(12L);
    }
}
