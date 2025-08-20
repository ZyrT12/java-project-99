package hexlet.code.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.tasks.TaskUpsertDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
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
@ActiveProfiles("test")
class TasksControllerTest {

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
    @WithMockUser
    void crudFlow() throws Exception {
        String statusPayload = "{\"name\":\"Planned\",\"slug\":\"planned\"}";
        MockHttpServletRequestBuilder createStatus = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(statusPayload);
        ResultActions createdStatus = mvc.perform(createStatus).andExpect(status().isCreated());
        String statusBody = createdStatus.andReturn().getResponse().getContentAsString();
        Long statusId = om.readTree(statusBody).get("id").asLong();

        TaskUpsertDto createDto = new TaskUpsertDto();
        createDto.setTitle("First task");
        createDto.setDescription("Desc");
        createDto.setTaskStatusId(statusId);

        MockHttpServletRequestBuilder createTask = post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createDto));
        ResultActions createdTask = mvc.perform(createTask)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("First task")))
                .andExpect(jsonPath("$.taskStatusId", is(statusId.intValue())));
        JsonNode createdTaskJson = om.readTree(createdTask.andReturn().getResponse().getContentAsString());
        Long taskId = createdTaskJson.get("id").asLong();

        mvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskId.intValue())))
                .andExpect(jsonPath("$.title", is("First task")));

        mvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)));

        TaskUpsertDto updateDto = new TaskUpsertDto();
        updateDto.setTitle("Updated task");
        updateDto.setDescription("New desc");
        updateDto.setTaskStatusId(statusId);

        mvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated task")));

        mvc.perform(delete("/api/tasks/{id}", taskId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/tasks/{id}", taskId)).andExpect(status().isNotFound());
    }
}
