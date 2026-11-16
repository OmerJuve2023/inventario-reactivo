package org.example.inventarioreactivo;

import org.example.inventarioreactivo.model.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ProductControllerTest {

    @Autowired
    WebTestClient testClient;

    @Test
    void createAndQueryProduct() {
        testClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductRequest("product01",
                        new BigDecimal("14.2"),
                        10))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("product01");
    }

    @Test
    void existProduct() {
        testClient.get()
                .uri("/api/v1/products/9999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void NegativePrice() {
        testClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ProductRequest("Clavos", new BigDecimal("-2"), 5))
                .exchange()
                .expectStatus().isBadRequest();
    }


}
