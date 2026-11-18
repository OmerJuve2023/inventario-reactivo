package org.example.inventarioreactivo;

import org.example.inventarioreactivo.model.Product;
import org.example.inventarioreactivo.model.dto.ProductRequest;
import org.example.inventarioreactivo.repository.InMemoryProductRepository;
import org.example.inventarioreactivo.service.imp.ProductServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImpTest {

    @Mock
    InMemoryProductRepository repository;

    @InjectMocks
    ProductServiceImp service;

    @Test
    void findByIdNotFound() {
        when(repository.findById(99L))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.findById(99L))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void findAll() {
        when(repository.findAll())
                .thenReturn(Flux.just(
                        new Product(1L,
                                "product01",
                                new BigDecimal("2.5"),
                                10)
                ));
        StepVerifier.create(service.findAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void createProduct() {
        ProductRequest request = new ProductRequest(
                "product02",
                new BigDecimal("4.2"),
                10
        );
        Product saved = new Product(
                1L,
                "product02",
                new BigDecimal("4.2"),
                10
        );

        when(repository.save(any(Product.class)))
                .thenReturn(Mono.just(saved));

        StepVerifier.create(service.create(request))
                .expectNext(saved)
                .verifyComplete();
    }

    @Test
    void updateProduct() {
        Product oldProduct = new Product(
                1L,
                "product01",
                new BigDecimal("2.5"),
                10
        );

        Product updateProduct = new Product(
                1L,
                "product02",
                new BigDecimal("4.12"),
                15
        );

        ProductRequest request = new ProductRequest(
                "product02",
                new BigDecimal("4.12"),
                15
        );
        when(repository.findById(1L))
                .thenReturn(Mono.just(oldProduct));

        when(repository.save(any(Product.class)))
                .thenReturn(Mono.just(updateProduct));

        StepVerifier.create(service.update(1L, request))
                .expectNext(updateProduct)
                .verifyComplete();
        verify(repository).findById(1L);
        verify(repository).save(any(Product.class));


    }

    @Test
    void deleteProduct() {
        Product product = new Product(
                1L,
                "product01",
                new BigDecimal("4.21"),
                45
        );
        when(repository.findById(1L))
                .thenReturn(Mono.just(product));

        when(repository.delete(1L))
                .thenReturn(Mono.just(true));

        StepVerifier.create(service.delete(1L))
                .verifyComplete();

        verify(repository).findById(1L);
        verify(repository).delete(1L);

    }

    @Test
    void createWithNegativePrice() {

    }


}
