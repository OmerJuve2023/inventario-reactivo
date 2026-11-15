package org.example.inventarioreactivo.repository;

import org.example.inventarioreactivo.model.Product;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository {
    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Flux<Product> findAll() {
        return Flux.defer(() -> Flux.fromIterable(store.values()));
    }

    public Mono<Product> findById(Long id) {
        return Mono.fromSupplier(() -> store.get(id));
    }

    public Mono<Product> save(Product p) {
        return Mono.fromSupplier(() -> {
            Long id = p.id() != null ? p.id() : sequence.incrementAndGet();
            Product saved = new Product(id, p.name(), p.price(), p.stock());
            store.put(id, saved);
            return saved;
        });
    }

    public Mono<Boolean> delete(Long id) {
        return Mono.fromSupplier(() -> store.remove(id) != null);
    }

}
