package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CookieValue;

//Метка по которой спринг находит контроллер
@RestController
public class SimpleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleController.class);

    @GetMapping(value = "/", produces = "application/json")
    public Message getHello() {
        return new Message("Hello Java!");
    }

    /**
     * Этот метод будет вызван в случае запроса POST /. В пути можно использовать * и парметры вида /user/${userId}
     * Метод может иметь различные сигнатуры(возвращаемое значение и параметры), в зависимости от ваших потребностей. Здесь,
     * в качестве возвращаемого значения, использован один из подклассов {@link org.springframework.http.HttpEntity },
     * который позволяет выставить заголовки и заполнить тело ответа
     *
     * @see PathVariable
     * @see RequestBody
     * @see CookieValue
     * @see javax.servlet.http.HttpSession
     */
    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity postHello(@RequestBody Message message) {
        LOGGER.info(String.format("got incoming message: %s", message.message));
        return ResponseEntity.ok().build();
    }

    /**
     * Объект класса будет автоматически преобразован в JSON при записи тела ответа.
     */
    @SuppressWarnings("unused")
    public static class Message {
        private String message;

        public Message(String message) {
            this.message = message;
        }

        /**
         *  Метод нужен для воссоздания объекта из json.
         */
        public Message() {
        }

        /**
         * Собирая через Refliction getter'ы объекта Jackson определяет список полей ответа. Можно использовать аннотации, чтобы уйти от
         * задания схемы объекта через naming-convention
         */
        public String getMessage() {
            return message;
        }

        /**
         *  Метод нужен для воссоздания объекта из json.
         */
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
