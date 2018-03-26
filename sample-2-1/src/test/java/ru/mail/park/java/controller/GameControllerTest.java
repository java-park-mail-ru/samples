package ru.mail.park.java.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mail.park.java.domain.User;
import ru.mail.park.java.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testMeRequiresLogin() {
        final ResponseEntity<User> meResp = testRestTemplate.getForEntity("/me", User.class);
        assertEquals(HttpStatus.UNAUTHORIZED, meResp.getStatusCode());
    }

    @Test
    void testLogin() {
        login();
    }

    private List<String> login() {
        when(userService.ensureUserExists(anyString())).thenReturn(new User("tester"));

        final ResponseEntity<User> loginResp = testRestTemplate.postForEntity("/login/tester", null, User.class);
        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        final List<String> cookies = loginResp.getHeaders().get("Set-Cookie");
        assertNotNull(cookies);
        assertFalse(cookies.isEmpty());

        final User user = loginResp.getBody();
        assertNotNull(user);
        assertEquals("tester", user.getName());

        return cookies;
    }

    @Test
    void testMe() {
        final List<String> cookies = login();

        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put(HttpHeaders.COOKIE, cookies);
        final HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);

        when(userService.getUser(anyString())).thenReturn(new User("tester"));
        final ResponseEntity<User> meResp = testRestTemplate.exchange("/me", HttpMethod.GET, requestEntity, User.class);

        assertEquals(HttpStatus.OK, meResp.getStatusCode());
        final User user = meResp.getBody();
        assertNotNull(user);
        assertEquals("tester", user.getName());
    }
}