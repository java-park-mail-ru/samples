package ru.mail.park.java.sample23mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
class CustomerControllerTest {

    private static final String JOHN_SMITH = "{\"firstName\": \"John\", \"lastName\": \"Smith\"}";
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testSave() throws Exception {
        mockMvc.perform(
                post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JOHN_SMITH)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(notNullValue()));
    }

    @Test
    void testSaveDuplicate() throws Exception {
        testSave();
        mockMvc.perform(
                post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JOHN_SMITH)
        ).andExpect(status().isConflict());
    }

    @Test
    void testFindByFirstNameEmpty() throws Exception {
        mockMvc.perform(get("/customer/name/John"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void testFindByFirstName() throws Exception {
        testSave();

        mockMvc.perform(get("/customer/name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("John"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    void testFindByLastNameEmpty() throws Exception {
        mockMvc.perform(get("/customer/Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testFindByLastName() throws Exception {
        testSave();

        mockMvc.perform(get("/customer/Smith"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0]lastName").value("Smith"));
    }
}