package hexlet.code.controllers;

import hexlet.code.dto.TaskStatusCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskStatusControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

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
        var dto = new TaskStatusCreateDto();
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
        var dto = new TaskStatusCreateDto();
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
        var dto = new TaskStatusCreateDto();
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
        // создаём
        var dto = new TaskStatusCreateDto();
        dto.setName("Old");
        dto.setSlug("old");
        var create = mvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        var id = om.readTree(create.getResponse().getContentAsString()).get("id").asLong();

        // меняем только name
        var patchJson = """
            {"name": "Renamed"}
        """;
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
        var dto = new TaskStatusCreateDto();
        dto.setName("Tmp");
        dto.setSlug("tmp");
        var create = mvc.perform(post("/api/task_statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();
        var id = om.readTree(create.getResponse().getContentAsString()).get("id").asLong();

        mvc.perform(delete("/api/task_statuses/{id}", id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/task_statuses/{id}", id))
                .andExpect(status().isNotFound());
    }
}