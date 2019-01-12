package ru.mail.park.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.java.domain.User;
import ru.mail.park.java.service.UserService;

import javax.servlet.http.HttpSession;


@RestController
public class GameController {
    private final UserService userService;

    @Autowired
    public GameController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login/{username}")
    public User login(@PathVariable String username, HttpSession session) {
        session.setAttribute("username", username);
        return userService.ensureUserExists(username);
    }

    @GetMapping("/me")
    public User me(HttpSession session) {
        final var username = (String) session.getAttribute("username");
        if (username == null) {
            throw new NotLoggedInException();
        }
        return userService.getUser(username);
    }

    @PostMapping("/gain")
    public User gain(HttpSession session) {
        final var me = me(session);
        return me.increasePower();
    }

    @PostMapping("/fight/{victim}")
    public User fight(@PathVariable String victim, HttpSession session) {
        final var me = me(session);
        final var other = userService.getUser(victim);
        if (other == null) {
            throw new UserNotFoundException();
        }
        if (me.getName().equals(other.getName())) {
            throw new IllegalStateException();
        }

        return me.fight(other);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static class NotLoggedInException extends RuntimeException {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private static class UserNotFoundException extends RuntimeException {
    }
}
