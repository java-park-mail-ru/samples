package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Solovyev on 17/04/2017.
 */
@SpringBootApplication
public class ResourcesExample {
    public static void main(String[] args) {
        SpringApplication.run(new Class[]{ResourcesExample.class, AppConfig.class}, args);
    }
}
