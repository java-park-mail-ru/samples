package mechanics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.park.Application;

/**
 * Created by Solovyev on 06/11/2016.
 */
@SpringBootApplication
@Import(Application.class)
@TestConfiguration
public class TestApplication {
}
