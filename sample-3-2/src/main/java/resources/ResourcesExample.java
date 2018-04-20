package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.SpringHandlerInstantiator;


@SpringBootApplication
public class ResourcesExample {
    public static void main(String[] args) {
        SpringApplication.run(ResourcesExample.class, args);
    }

    @Bean
    public ObjectMapper getObjectMapper(SpringHandlerInstantiator springHandlerInstantiator) {
        return Jackson2ObjectMapperBuilder.json()
                .failOnUnknownProperties(false)
                .handlerInstantiator(springHandlerInstantiator)
                .build();
    }


    @Bean
    public SpringHandlerInstantiator getSpringHandlerInstantiator(AutowireCapableBeanFactory beanFactory) {
        return new SpringHandlerInstantiator(beanFactory);
    }
}
