package com.nttdata.bbva.product.controllers;

import com.nttdata.bbva.product.documents.ProductType;
import com.nttdata.bbva.product.services.IProductTypeService;
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
@RequestMapping("api/1.0.0/producttypes")
public class ProductTypeController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private IProductTypeService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<ProductType>>> findAll(){
		logger.info("Inicio ProductTypeController ::: findAll");
		Flux<ProductType> productTypes = service.findAll().doOnNext(x -> logger.info("Fin ProductTypeController ::: findAll"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productTypes));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Mono<ProductType>>> findById(@PathVariable("id") String id){
		logger.info("Inicio ProductTypeController ::: findById");
		Mono<ProductType> productType = service.findById(id).doOnNext(x -> logger.info("Fin ProductTypeController ::: findById"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productType));
	}
	
	@PostMapping
	public Mono<ResponseEntity<Mono<ProductType>>> insert(@Valid @RequestBody ProductType obj){
		logger.info("Inicio ProductTypeController ::: insert");
		Mono<ProductType> productType = service.insert(obj).doOnNext(x -> logger.info("Fin ProductTypeController ::: insert"));
		return Mono.just(new ResponseEntity<Mono<ProductType>>(productType, HttpStatus.CREATED));
	}
	
	@PutMapping
	public Mono<ResponseEntity<Mono<ProductType>>> update(@Valid @RequestBody ProductType obj){
		logger.info("Inicio ProductTypeController ::: update");
		Mono<ProductType> productType = service.update(obj).doOnNext(x -> logger.info("Fin ProductTypeController ::: update"));
		return Mono.just(new ResponseEntity<Mono<ProductType>>(productType, HttpStatus.CREATED));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
		logger.info("Inicio ProductTypeController ::: delete");
		service.delete(id).doOnNext(x -> logger.info("Fin ProductTypeController ::: delete"));
		return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}
}
