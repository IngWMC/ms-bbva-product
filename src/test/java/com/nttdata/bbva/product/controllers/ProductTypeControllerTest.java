package com.nttdata.bbva.product.controllers;

import com.nttdata.bbva.product.documents.ProductType;
import com.nttdata.bbva.product.services.IProductTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductTypeControllerTest {
    @MockBean
    private IProductTypeService service;
    @Autowired
    private WebTestClient webTestClient;
    ProductType productType;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:7072/api/1.0.0/producttypes").build();
        productType = ProductType.builder()
                .id("1")
                .name("Créditos")
                .shortName("CRE")
                .build();
    }

    @Test
    void findAll() {
        Mockito.when(service.findAll()).thenReturn(Flux.just(productType));
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fintById() {
        Mockito.when(service.findById(Mockito.any())).thenReturn(Mono.just(productType));
        webTestClient.get().uri("/{id}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("id").isEqualTo("1");
        //.expectBody(Product.class).isEqualTo(product);
    }

    @Test
    void insert() {
        Mockito.when(service.insert(Mockito.any())).thenReturn(Mono.just(productType));
        webTestClient.post()
                .uri("/")
                .bodyValue(productType)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void update() {
        Mockito.when(service.update(Mockito.any())).thenReturn(Mono.just(productType));
        webTestClient.put()
                .uri("/")
                .bodyValue(productType)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void delete() {
        Mockito.when(service.delete(Mockito.any())).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri("/{id}", "1")
                .exchange()
                .expectStatus().isNoContent();
    }
}