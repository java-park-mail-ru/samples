package ru.park.mail.java.sample22jdbcspring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import ru.park.mail.java.sample22jdbcspring.domain.House;
import ru.park.mail.java.sample22jdbcspring.domain.Person;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class SampleControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SampleDao dao;
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Test
	public void testCreatePerson() throws Exception {
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
	public void testCreateHouse() throws Exception {
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
	public void testCreateDuplicateHouse() throws Exception {
		testCreateHouse();
		expected.expect(NestedServletException.class);
		mockMvc.perform(put("/house").param("name", "testName")).andExpect(status().is4xxClientError());
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
