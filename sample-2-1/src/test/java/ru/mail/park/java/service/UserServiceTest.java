package ru.mail.park.java.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mail.park.java.domain.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void ensureExists() {
        final var tester = userService.ensureUserExists("tester");
        assertNotNull(tester);
        assertSame(tester, userService.ensureUserExists("tester"), "User doesn't exist");
    }
}