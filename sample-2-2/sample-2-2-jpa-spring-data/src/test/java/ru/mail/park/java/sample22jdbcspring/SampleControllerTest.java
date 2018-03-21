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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public abstract class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreatePerson() throws Exception {
        IdMatcherGetter idGetter = new IdMatcherGetter();
        mockMvc.perform(put(getBasePath() + "/person").param("name", "testName")).andExpect(status().isOk())
                .andExpect(jsonPath("name").value("testName")).andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("id").value(idGetter)).andExpect(jsonPath("houseId").doesNotExist())
                .andExpect(jsonPath("motherId").doesNotExist()).andExpect(jsonPath("fartherId").doesNotExist());
        assertFalse(0 == idGetter.id);
        Person created = getDao().getPerson(idGetter.id);
        assertNotNull(created);
    }

    @Test
    void testCreateHouse() throws Exception {
        IdMatcherGetter idGetter = new IdMatcherGetter();
        mockMvc.perform(put(getBasePath() + "/house").param("name", "testName")).andExpect(status().isOk())
                .andExpect(jsonPath("name").value("testName")).andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("id").value(idGetter)).andExpect(jsonPath("words").doesNotExist())
                .andExpect(jsonPath("sigil").doesNotExist()).andExpect(jsonPath("sigil").doesNotExist());
        assertFalse(0 == idGetter.id);
        House created = getDao().getHouse(idGetter.id);
        assertNotNull(created);
    }

    @Test
    void testCreateDuplicateHouse() throws Exception {
        testCreateHouse();
        assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(put(getBasePath() + "/house").param("name", "testName")).andExpect(status().is4xxClientError());
        });
    }

    protected abstract SampleDao getDao();

    protected abstract String getBasePath();


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
