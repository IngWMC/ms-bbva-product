package com.nttdata.bbva.product.services.impl;

import com.nttdata.bbva.product.documents.ProductType;
import com.nttdata.bbva.product.repositories.IProductTypeRepository;
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
class ProductTypeServiceImplTest {
    @InjectMocks
    ProductTypeServiceImpl service;
    @Mock
    private IProductTypeRepository repo;

    ProductType productType;

    @BeforeEach
    void setUp() {
        productType = ProductType.builder()
                .id("1")
                .name("Cuenta bancaria")
                .shortName("CUEB")
                .build();
    }

    @Test
    void insert() {
        Mockito.when(repo.save(Mockito.any())).thenReturn(Mono.just(productType));
        Mono<ProductType> response = service.insert(productType);

        StepVerifier.create(response)
                .expectNextMatches(p -> p.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void update() {
        Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Mono.just(productType));
        Mockito.when(repo.save(Mockito.any())).thenReturn(Mono.just(productType));
        Mono<ProductType> response = service.update(productType);

        StepVerifier.create(response)
                .expectNextMatches(p -> p.getShortName().equals("CUEB"))
                .verifyComplete();
    }

    @Test
    void findAll() {
        Mockito.when(repo.findAll()).thenReturn(Flux.just(productType));
        Flux<ProductType> response = service.findAll();

        StepVerifier.create(response)
                .consumeNextWith(p -> {
                    assertEquals("CUEB", p.getShortName());
                })
                .expectComplete();
    }

    @Test
    void findById() {
        Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Mono.just(productType));
        Mono<ProductType> response = service.findById("1");

        StepVerifier.create(response)
                .consumeNextWith(p -> {
                    assertEquals("Cuenta bancaria", p.getName());
                })
                .expectComplete();
    }

    @Test
    void delete() {
        Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Mono.just(productType));
        Mockito.when(repo.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
        Mono<Void> response = service.delete("1");

        StepVerifier.create(response)
                .verifyComplete();
    }
}