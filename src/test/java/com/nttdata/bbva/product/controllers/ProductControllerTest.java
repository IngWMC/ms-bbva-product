package com.nttdata.bbva.product.controllers;

import com.nttdata.bbva.product.documents.Product;
import com.nttdata.bbva.product.services.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
//@WebFluxTest(controllers = ProductController.class)
class ProductControllerTest {
    @MockBean
    private IProductService service;
    @Autowired
    private WebTestClient webTestClient;
    Product product;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:7072/api/1.0.0/products").build();
        product = Product.builder()
                .id("1")
                .name("Cuenta a plazo fijo")
                .productTypeId("1")
                .shortName("CUEPF")
                .isMonthlyMaintenance("true")
                .maximumLimitMonthlyMovements(0)
                .build();
    }

    @Test
    void findAll() {
        Mockito.when(service.findAll()).thenReturn(Flux.just(product));
        webTestClient
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void fintById() {
        Mockito.when(service.findById(Mockito.any())).thenReturn(Mono.just(product));
        webTestClient.get().uri("/{id}", "1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("id").isEqualTo("1");
                //.expectBody(Product.class).isEqualTo(product);
    }

    @Test
    void insert() {
        Mockito.when(service.insert(Mockito.any())).thenReturn(Mono.just(product));
        webTestClient.post()
                .uri("/")
                .bodyValue(product)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void update() {
        Mockito.when(service.update(Mockito.any())).thenReturn(Mono.just(product));
        webTestClient.put()
                .uri("/")
                .bodyValue(product)
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