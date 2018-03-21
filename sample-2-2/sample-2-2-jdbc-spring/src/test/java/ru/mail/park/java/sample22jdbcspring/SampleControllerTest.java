package ru.mail.park.java.sample22jdbcspring;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;
import ru.mail.park.java.sample22jdbcspring.domain.House;
import ru.mail.park.java.sample22jdbcspring.domain.Person;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SampleDao dao;

    @Test
    void testCreatePerson() throws Exception {
        IdMatcherGetter idGetter = new IdMatcherGetter();
        mockMvc.perform(put("/person").param("name", "testName")).andExpect(status().isOk())
                .andExpect(jsonPath("name").value("testName")).andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("id").value(idGetter)).andExpect(jsonPath("houseId").doesNotExist())
                .andExpect(jsonPath("motherId").doesNotExist()).andExpect(jsonPath("fartherId").doesNotExist());
        assertFalse(0 == idGetter.id);
        Person created = dao.getPerson(idGetter.id);
        assertNotNull(created);
    }

    @Test
    void testCreateHouse() throws Exception {
        IdMatcherGetter idGetter = new IdMatcherGetter();
        mockMvc.perform(put("/house").param("name", "testName")).andExpect(status().isOk())
                .andExpect(jsonPath("name").value("testName")).andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("id").value(idGetter)).andExpect(jsonPath("words").doesNotExist())
                .andExpect(jsonPath("sigil").doesNotExist()).andExpect(jsonPath("sigil").doesNotExist());
        assertFalse(0 == idGetter.id);
        House created = dao.getHouse(idGetter.id);
        assertNotNull(created);
    }

    @Test
    void testCreateDuplicateHouse() throws Exception {
        testCreateHouse();
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(put("/house").param("name", "testName")).andExpect(status().is4xxClientError());
        });
    }

    private static class IdMatcherGetter extends BaseMatcher<Number> {
        private long id;

        @Override
        public boolean matches(Object item) {
            id = ((Number) item).longValue();
            return true;
        }

        @Override
        public void describeTo(Description description) {
            // no code
        }
    }

}
