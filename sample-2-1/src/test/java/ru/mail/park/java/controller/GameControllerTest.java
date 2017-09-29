package ru.mail.park.java.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import ru.mail.park.java.domain.User;
import ru.mail.park.java.service.UsersService;

/**
 * Created by isopov on 29.09.16.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GameControllerTest {
    @MockBean
    private UsersService usersService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMeRequiresLogin() {
        ResponseEntity<User> meResp = restTemplate.getForEntity("/me", User.class);
        assertEquals(HttpStatus.UNAUTHORIZED, meResp.getStatusCode());
    }

    @Test
    public void testLogin() {
        login();
    }

    private List<String> login() {
        when(usersService.ensureUserExists(anyString())).thenReturn(new User("foo"));

        ResponseEntity<User> loginResp = restTemplate.postForEntity("/login/foo",null, User.class);
        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        List<String> coockies = loginResp.getHeaders().get("Set-Cookie");
        assertNotNull(coockies);
        assertFalse(coockies.isEmpty());

        User user = loginResp.getBody();
        assertNotNull(user);

        assertEquals("foo", user.getName());

        return coockies;
    }


    @Test
    public void testMe() {
        List<String> coockies = login();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, coockies);
        HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);

        when(usersService.getUser(anyString())).thenReturn(new User("foo"));
        ResponseEntity<User> meResp = restTemplate.exchange("/me", HttpMethod.GET, requestEntity, User.class);

        assertEquals(HttpStatus.OK, meResp.getStatusCode());
        User user = meResp.getBody();
        assertNotNull(user);
        assertEquals("foo", user.getName());
    }
}