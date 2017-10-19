package ru.mail.park.java.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ru.mail.park.java.service.UsersService;

/**
 * Created by isopov on 29.09.16.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class MockGameControllerTest {
	@SpyBean
	private UsersService usersService;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup() {
		usersService.ensureUserExists("foo");
	}

	@Test
	public void testMeRequiresLogin() throws Exception {

		mockMvc
				.perform(get("/me"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testMe() throws Exception {
		mockMvc
				.perform(get("/me").sessionAttr("username", "foo"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").value("foo"))
				.andExpect(jsonPath("wins").value(0))
				.andExpect(jsonPath("power").value(0));
		verify(usersService).getUser("foo");
	}

}