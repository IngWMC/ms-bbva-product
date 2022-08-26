package com.nttdata.bbva.product.services.impl;

import com.nttdata.bbva.product.documents.Product;
import com.nttdata.bbva.product.documents.ProductType;
import com.nttdata.bbva.product.repositories.IProductRepository;
import com.nttdata.bbva.product.services.IProductTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private IProductRepository repo;
    @Mock
    private IProductTypeService productTypeService;

    Product product;
    Product productWithoutId;
    ProductType productType;

    @BeforeEach
    void setUp() {
        productType = ProductType.builder()
                .id("1")
                .name("Cuenta bancaria")
                .shortName("CUEB")
                .build();
        product = Product.builder()
                .id("1")
                .name("Cuenta a plazo fijo")
                .productTypeId("1")
                .shortName("CUEPF")
                .build();
        productWithoutId = Product.builder()
                .name("Cuenta a plazo fijo")
                .productTypeId("1")
                .shortName("CUEPF")
                .build();
    }

    @Test
    void insert() {
        Mockito.when(productTypeService.findById(Mockito.anyString())).thenReturn(Mono.just(productType));
        Mockito.when(repo.save(Mockito.any())).thenReturn(Mono.just(product));
        Mono<Product> response = productService.insert(product);

        StepVerifier.create(response)
                .expectNextMatches(p -> p.getProductType().equals(productType))
                .verifyComplete();
    }

    @Test
    void update() {
        Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Mono.just(product));
        Mockito.when(productTypeService.findById(Mockito.anyString())).thenReturn(Mono.just(productType));
        Mockito.when(repo.save(Mockito.any())).thenReturn(Mono.just(product));
        Mono<Product> response = productService.update(product);

        StepVerifier.create(response)
                .expectNextMatches(p -> p.getProductType().equals(productType))
                .verifyComplete();
    }

    @Test
    void updateWithoutIdError() {
        Mono<Product> response = productService.update(productWithoutId);

        StepVerifier.create(response)
                .consumeErrorWith(error -> {
                    assertEquals("El campo id es requerido.", error.getMessage());
                })
                //.expectErrorMatches(throwable -> throwable instanceof BadRequestException)
                .verify();
    }

    @Test
    void findAll() {
        Mockito.when(productTypeService.findById(Mockito.anyString())).thenReturn(Mono.just(productType));
        Mockito.when(repo.findAll()).thenReturn(Flux.just(product));
        Flux<Product> response = productService.findAll();
        StepVerifier.create(response)
                .expectNextMatches(p -> p.getShortName().equals("CUEPF"))
                .verifyComplete();
    }

    @Test
    void findById() {
        Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Mono.just(product));
        Mockito.when(productTypeService.findById(Mockito.anyString())).thenReturn(Mono.just(productType));
        Mono<Product> response = productService.findById("1");

        StepVerifier.create(response)
                .consumeNextWith(p -> {
                    assertEquals("1", p.getId());
                })
                .verifyComplete();
    }

    @Test
    void delete() {
        Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Mono.just(product));
        Mockito.when(repo.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
        Mono<Void> response = productService.delete("1");

        StepVerifier.create(response)
                .verifyComplete();
    }
}