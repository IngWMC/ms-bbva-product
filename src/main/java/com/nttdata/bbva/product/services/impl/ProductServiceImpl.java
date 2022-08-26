package com.nttdata.bbva.product.services.impl;

import com.nttdata.bbva.product.documents.Product;
import com.nttdata.bbva.product.exceptions.BadRequestException;
import com.nttdata.bbva.product.exceptions.ModelNotFoundException;
import com.nttdata.bbva.product.repositories.IProductRepository;
import com.nttdata.bbva.product.services.IProductService;
import com.nttdata.bbva.product.services.IProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.OptionalLong;

@Service
public class ProductServiceImpl implements IProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private IProductRepository repo;
	
	@Autowired
	private IProductTypeService productTypeService;
	
	@Override
	public Mono<Product> insert(Product obj) {
		return productTypeService.findById(obj.getProductTypeId())
				.switchIfEmpty(Mono.error(() -> new BadRequestException("El campo productTypeId tiene un valor no válido.")))
				.flatMap(productType -> repo.save(obj)
						.map(product -> {
							product.setProductType(productType);
							return product;
						})
				)
				.doOnNext(p -> logger.info("SE INSERTÓ EL PRODUCTO ::: " + p.getId()));
	}

	@Override
	public Mono<Product> update(Product obj) {
		if (obj.getId() == null || obj.getId().isEmpty())
			return Mono.error(() -> new BadRequestException("El campo id es requerido."));

		return repo.findById(obj.getId())
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("CLIENTE NO ENCONTRADO")))
				.flatMap(p -> productTypeService.findById(obj.getProductTypeId())
						.switchIfEmpty(Mono.error(() -> new BadRequestException("El campo productTypeId tiene un valor no válido.")))
						.flatMap(productType -> repo.save(obj)
								.map(product -> {
									product.setProductType(productType);
									return product;
								})
						)
				)
				.doOnNext(p -> logger.info("SE ACTUALIZÓ EL PRODUCTO ::: " + p.getId()));
	}

	@Override
	public Flux<Product> findAll() {
		return repo.findAll()
				.flatMap(product -> productTypeService.findById(product.getProductTypeId())
						.map(productType -> {
							product.setProductType(productType);
							return product;
						})
				);
	}

	@Override
	public Mono<Product> findById(String id) {
		return repo.findById(id)
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("PRODUCTO NO ENCONTRADO")))
				.flatMap(product -> productTypeService.findById(product.getProductTypeId())
						.map(productType -> {
							product.setProductType(productType);
							return product;
						})
				)
				.doOnNext(p -> logger.info("SE ENCONTRÓ EL PRODUCTO ::: " + id));
	}

	@Override
	public Mono<Void> delete(String id) {
		return repo.findById(id)
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("PRODUCTO NO ENCONTRADO")))
				.flatMap(product -> repo.deleteById(product.getId()))
				.doOnNext(p -> logger.info("SE ELIMINÓ EL PRODUCTO ::: " + id));
	}

}
