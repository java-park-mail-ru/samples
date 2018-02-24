package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MarkController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkController.class);
    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @PostMapping(value = "/sign", consumes = "application/json")
    public ResponseEntity postHello(HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            userId = ID_GENERATOR.getAndIncrement();
            httpSession.setAttribute("userId", userId);
        }
        LOGGER.info(String.format("got incoming message from user: %d", userId));
        return ResponseEntity.ok().build();
    }
}
