package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.tasks.TaskStatusCreateDto;
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

    public MockMvc getMvc() {
        return mvc;
    }

    public ObjectMapper getOm() {
        return om;
    }

    @Test
    void listIsPublic() throws Exception {
        mvc.perform(get("/api/task_statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getByIdNotFound() throws Exception {
        mvc.perform(get("/api/task_statuses/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRequiresAuth() throws Exception {
        TaskStatusCreateDto dto = new TaskStatusCreateDto();
        dto.setName("New");
        dto.setSlug("new");
        mvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    void createOk() throws Exception {
        TaskStatusCreateDto dto = new TaskStatusCreateDto();
        dto.setName("New");
        dto.setSlug("new");
        mvc.perform(post("/api/task_statuses")
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
    void uniqueness() throws Exception {
        TaskStatusCreateDto dto = new TaskStatusCreateDto();
        dto.setName("Unique");
        dto.setSlug("unique");
        mvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "user")
    void partialUpdate() throws Exception {
        TaskStatusCreateDto dto = new TaskStatusCreateDto();
        dto.setName("Old");
        dto.setSlug("old");
        String created = mvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new ObjectMapper().readTree(created).get("id").asLong();

        String patchJson = "{\"name\":\"Renamed\"}";
        mvc.perform(put("/api/task_statuses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Renamed")))
                .andExpect(jsonPath("$.slug", is("old")));
    }

    @Test
    @WithMockUser(username = "user")
    void deleteOk() throws Exception {
        TaskStatusCreateDto dto = new TaskStatusCreateDto();
        dto.setName("Tmp");
        dto.setSlug("tmp");
        String created = mvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long id = new ObjectMapper().readTree(created).get("id").asLong();

        mvc.perform(delete("/api/task_statuses/{id}", id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/task_statuses/{id}", id))
                .andExpect(status().isNotFound());
    }
}
