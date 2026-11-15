package org.example.inventarioreactivo.service.imp;

import org.example.inventarioreactivo.model.Product;
import org.example.inventarioreactivo.model.dto.ProductRequest;
import org.example.inventarioreactivo.repository.InMemoryProductRepository;
import org.example.inventarioreactivo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImp implements ProductService {

    private final InMemoryProductRepository repository;

    public ProductServiceImp(InMemoryProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Product> findById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product " + id + " no exist"
                )));
    }

    @Override
    public Mono<Product> create(ProductRequest request) {
        return validate(request).then(Mono.defer(
                () -> repository.save(new Product(null,
                        request.name(),
                        request.price(),
                        request.stock()))
        ));
    }

    @Override
    public Mono<Product> update(Long id, ProductRequest request) {
        return validate(request).then(
                findById(id)
                        .flatMap(
                                old -> repository.save(new Product(
                                        old.id(),
                                        request.name(),
                                        request.price(),
                                        request.stock()
                                ))
                        )
        );
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(p -> repository.delete(p.id())).then();
    }

    private Mono<Void> validate(ProductRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is obligatory"));
        }
        if (request.price() == null || request.price().signum() < 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must be >=0"));
        }
        if (request.stock() < 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "stock must be >=0"));
        }
        return Mono.empty();
    }
}
