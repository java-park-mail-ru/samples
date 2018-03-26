package ru.mail.park.java.sample23mongo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class ReactiveCustomerControllerTest {

    private static final String JOHN_SMITH = "{\"firstName\": \"John\", \"lastName\": \"Smith\"}";
    @Autowired
    private ReactiveCustomerRepository repository;
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        repository.deleteAll().block();
    }

    @Test
    void testSave() {
        webTestClient.post().uri("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(JOHN_SMITH))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8");
    }

    @Test
    void testSaveDuplicate() {
        testSave();
        webTestClient.post().uri("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(JOHN_SMITH))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testFindByFirstNameEmpty() {
        webTestClient.get().uri("/customer/name/John")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testFindByFirstName() {
        testSave();

        webTestClient.get().uri("/customer/name/John")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("firstName").isEqualTo("John")
                .jsonPath("lastName").isEqualTo("Smith");
    }

    @Test
    void testFindByLastNameEmpty() {
        webTestClient.get().uri("/customer/Smith")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isEmpty();
    }

    @Test
    void testFindByLastName() {
        testSave();

        webTestClient.get().uri("/customer/Smith")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].firstName").isEqualTo("John")
                .jsonPath("$[0].lastName").isEqualTo("Smith")
                .jsonPath("$[1]").doesNotHaveJsonPath();
    }
}