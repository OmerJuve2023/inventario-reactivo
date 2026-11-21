package org.example.inventarioreactivo.controller;

import jakarta.validation.Valid;
import org.example.inventarioreactivo.model.Product;
import org.example.inventarioreactivo.model.dto.ProductRequest;
import org.example.inventarioreactivo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> list() {
        return productService.findAll();
    }

    @GetMapping({"/{id}"})
    public Mono<Product> get(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> create(@Valid @RequestBody ProductRequest productRequest) {
        return productService.create(productRequest);
    }

    @PutMapping("/{id}")
    public Mono<Product> update(@PathVariable Long id,@Valid @RequestBody ProductRequest productRequest) {
        return productService.update(id, productRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return productService.delete(id);
    }
}
