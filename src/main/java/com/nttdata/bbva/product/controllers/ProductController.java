package com.nttdata.bbva.product.controllers;

import com.nttdata.bbva.product.documents.Product;
import com.nttdata.bbva.product.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/1.0.0/products")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private IProductService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Product>>> findAll() {
		logger.info("Inicio ProductController ::: findAll");
		Flux<Product> products = service.findAll().doOnNext(x -> logger.info("Fin ProductController ::: findAll"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(products));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Mono<Product>>> fintById(@PathVariable("id") String id) {
		logger.info("Inicio ProductController ::: fintById");
		Mono<Product> product = service.findById(id).doOnNext(x -> logger.info("Fin ProductController ::: findById"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(product));
	}
	
	@PostMapping
	public Mono<ResponseEntity<Mono<Product>>> insert(@Valid @RequestBody Product obj) {
		logger.info("Inicio ProductController ::: insert");
		Mono<Product> product = service.insert(obj).doOnNext(x -> logger.info("Fin ProductController ::: insert"));
		return Mono.just(new ResponseEntity<Mono<Product>>(product, HttpStatus.CREATED));
	}
	
	@PutMapping
	public Mono<ResponseEntity<Mono<Product>>> update(@Valid @RequestBody Product obj) {
		logger.info("Inicio ProductController ::: update");
		Mono<Product> product = service.update(obj).doOnNext(x -> logger.info("Fin ProductController ::: update"));
		return Mono.just(new ResponseEntity<Mono<Product>>(product, HttpStatus.CREATED));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
		logger.info("Inicio ProductController ::: delete");
		service.delete(id).doOnNext(x -> logger.info("Fin ProductController ::: delete"));
		return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}
}
