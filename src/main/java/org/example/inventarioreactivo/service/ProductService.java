package org.example.inventarioreactivo.service;

import org.example.inventarioreactivo.model.Product;
import org.example.inventarioreactivo.model.dto.ProductRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    public Flux<Product> findAll();

    public Mono<Product> findById(Long id);

    public Mono<Product> create(ProductRequest request);

    public Mono<Product> update(Long id, ProductRequest request);

    public Mono<Void> delete(Long id);
}
