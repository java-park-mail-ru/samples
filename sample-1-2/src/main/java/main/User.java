package main;

//импорты появятся автоматически, если вы выбираете класс из выпадающего списка или же после alt+enter
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Solovyev on 06/09/16.
 */

//Метка по которой спринг находит контроллер
@RestController
public class User {

    private String lastLogin;

    /**
     * Этот метод будет вызван в случае корневого запроса. В пути можно использовать * и парметры вида /user/${userId}
     * Метод может иметь различные сигнатуры, в зависимости от ваших потребностей. В данном случае использован один из
     * подклассов {@link org.springframework.http.HttpEntity }, который позволяет выставить заголовки
     * и заполнить тело ответа
     */

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity login(@RequestParam(name = "lastLogin", required = false) String login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse(lastLogin));
        }
        this.lastLogin = login;

        return ResponseEntity.ok(new SuccessResponse(login));
    }


    // объект класса будет автоматически преобразован в JSON при записи тела ответа
    private static final class SuccessResponse {
        private String login;

        private SuccessResponse(String login) {
            this.login = login;
        }

        //Функция необходима для преобразования см  https://en.wikipedia.org/wiki/Plain_Old_Java_Object
        @SuppressWarnings("unused")
        public String getLogin() {
            return login;
        }
    }

    private static final class BadResponse {
        private String lastLogin;

        private BadResponse(String login) {
            this.lastLogin = login;
        }

        //Справа, на полосе прокрутки, можно увидеть желтые метки.
        //Это потенциальные ошибки и места, которые можно улучшить или инспекции
        //Если вы уверены в том как написан ваш код (только в этом случае), то можете убрать их. Это назвыется подавление инспекций
        @SuppressWarnings("unused")
        public String getLastLogin() {
            return lastLogin;
        }
    }
}
