package hexlet.code.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerExtraTest.DummyController.class)
class GlobalExceptionHandlerExtraTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @RestController
    @Validated
    static class DummyController {
        @GetMapping("/err-entity")
        String errEntity() {
            throw new EntityNotFoundException("not found");
        }

        @GetMapping("/err-rse")
        String errRse() {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "boom");
        }

        static class Payload {
            @NotBlank
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


        @PostMapping(path = "/err-validation", consumes = "application/json")
        String errValidation(@Valid @RequestBody Payload payload) {
            return "ok";
        }
    }

    @Test
    void entityNotFoundIs500WithErrorBody() throws Exception {
        mvc.perform(get("/err-entity"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void responseStatusExceptionIs500WithErrorBody() throws Exception {
        mvc.perform(get("/err-rse"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void validationFailureIs500WithErrorBody() throws Exception {
        String body = objectMapper.writeValueAsString(new DummyController.Payload());
        mvc.perform(post("/err-validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());
    }
}
