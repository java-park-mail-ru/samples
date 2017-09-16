package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Solovyev on 06/09/16.
 */


/*
 Включает аутоконфигурацию на основе зависимостей
 и поиск компонентов Spring (@SpringBootConfiguration, @EnableAutoConfiguration)
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //Старт приложения. Здесь стартует embedded jetty server.
        // Spring подключает к Jetty Dispatcher Servlet, который обрабатывает HTTP-запросы пользователей
        SpringApplication.run(Main.class, args);
    }
}
