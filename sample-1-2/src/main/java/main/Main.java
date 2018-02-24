package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 Включает аутоконфигурацию на основе зависимостей
 и поиск компонентов Spring (@SpringBootConfiguration, @EnableAutoConfiguration)
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //Старт приложения. Здесь стартует embedded Tomcat server.
        // Spring подключает к Tomcat'у Dispatcher Servlet, который обрабатывает HTTP-запросы пользователей
        SpringApplication.run(Main.class, args);
    }
}
