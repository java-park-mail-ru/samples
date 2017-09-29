package ru.mail.park.java.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.mail.park.java.domain.User;
import ru.mail.park.java.service.UsersService;

/**
 * Created by isopov on 29.09.16.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RetrofitGameControllerTest {
	@MockBean
	private UsersService usersService;
	@LocalServerPort
	private int localPort;
	private Game game;

	@Before
	public void setup() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://localhost:" + localPort)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		game = retrofit.create(Game.class);
	}

	@Test
	public void testMeRequiresLogin() throws IOException {
		Response<User> response = game.me().execute();
		assertEquals(UNAUTHORIZED.value(), response.code());
	}

	@Test
	public void testLogin() throws IOException {
		login();
	}

	private String login() throws IOException {
		when(usersService.ensureUserExists(anyString())).thenReturn(new User("foo"));

		Response<User> response = game.login("foo").execute();
		assertTrue(response.isSuccessful());
		assertEquals("foo", response.body().getName());

		String coockie = response.headers().get("Set-Cookie");
		assertNotNull(coockie);
		return coockie;
	}

	@Test
	public void testMe() throws IOException {
		String cookie = login();

		when(usersService.getUser(anyString())).thenReturn(new User("foo"));
		Response<User> response = game.me(cookie).execute();
		assertTrue(response.isSuccessful());
		assertEquals("foo", response.body().getName());
	}

	public interface Game {
		@GET("/me")
		Call<User> me();

		@GET("/me")
		Call<User> me(@Header("Cookie") String cookie);

		@POST("/login/{name}")
		Call<User> login(@Path("name") String name);
	}
}