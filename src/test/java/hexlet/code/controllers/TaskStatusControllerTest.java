package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.tasks.TaskStatusUpsertDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskStatusControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void listIsPublic() throws Exception {
        mvc.perform(get("/api/task-statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getByIdNotFound() throws Exception {
        mvc.perform(get("/api/task-statuses/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRequiresAuth() throws Exception {
        TaskStatusUpsertDto dto = new TaskStatusUpsertDto();
        dto.setName("New");
        dto.setSlug("new");
        mvc.perform(post("/api/task-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    void createOk() throws Exception {
        TaskStatusUpsertDto dto = new TaskStatusUpsertDto();
        dto.setName("New");
        dto.setSlug("new");
        mvc.perform(post("/api/task-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("New")))
                .andExpect(jsonPath("$.slug", is("new")))
                .andExpect(jsonPath("$.createdAt", notNullValue()));
    }

    @Test
    @WithMockUser(username = "user")
    void partialUpdate() throws Exception {
        TaskStatusUpsertDto dto = new TaskStatusUpsertDto();
        dto.setName("Old");
        dto.setSlug("old");
        String created = mvc.perform(post("/api/task-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new ObjectMapper().readTree(created).get("id").asLong();

        String putJson = "{\"name\":\"Renamed\"}";
        mvc.perform(put("/api/task-statuses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(putJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Renamed")))
                .andExpect(jsonPath("$.slug", is("old")));
    }

    @Test
    @WithMockUser(username = "user")
    void deleteOk() throws Exception {
        TaskStatusUpsertDto dto = new TaskStatusUpsertDto();
        dto.setName("Tmp");
        dto.setSlug("tmp");
        String created = mvc.perform(post("/api/task-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new ObjectMapper().readTree(created).get("id").asLong();

        mvc.perform(delete("/api/task-statuses/{id}", id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/task-statuses/{id}", id))
                .andExpect(status().isNotFound());
    }
}
