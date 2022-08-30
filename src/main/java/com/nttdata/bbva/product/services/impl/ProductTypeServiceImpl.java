package com.nttdata.bbva.product.services.impl;

import com.nttdata.bbva.product.documents.ProductType;
import com.nttdata.bbva.product.exceptions.BadRequestException;
import com.nttdata.bbva.product.exceptions.ModelNotFoundException;
import com.nttdata.bbva.product.repositories.IProductTypeRepository;
import com.nttdata.bbva.product.services.IProductTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductTypeServiceImpl implements IProductTypeService {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Autowired
	private IProductTypeRepository repo;
	
	@Override
	public Mono<ProductType> insert(ProductType obj) {
		return repo.save(obj)
				.doOnNext(pt -> logger.info("SE INSERTÓ EL TIPO PRODUCTO - ID = " + pt.getId()));
	}

	@Override
	public Mono<ProductType> update(ProductType obj) {
		if (obj.getId() == null || obj.getId().isEmpty())
			return Mono.error(() -> new BadRequestException("El campo id es requerido."));
		
		return repo.findById(obj.getId())
				.switchIfEmpty(Mono.error(() -> new BadRequestException("El campo id no es válido.")))
				.flatMap(productType -> {
					return repo.save(obj);
				})
				.doOnNext(pt -> logger.info("SE ACTUALIZÓ EL TIPO PRODUCTO - ID = " + pt.getId()));
	}

	@Override
	public Flux<ProductType> findAll() {
		return repo.findAll();
	}

	@Override
	public Mono<ProductType> findById(String id) {
		return repo.findById(id)
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("TIPO PRODUCTO NO ENCONTRADO")))
				.doOnNext(pt -> logger.info("SE ENCONTRÓ EL TIPO PRODUCTO - ID = " + id));
	}

	@Override
	public Mono<Void> delete(String id) {
		return repo.findById(id)
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("TIPO PRODUCTO NO ENCONTRADO")))
				.flatMap(productType -> repo.deleteById(productType.getId()))
				.doOnNext(pt -> logger.info("SE ELIMINÓ EL CLIENTE - ID = " + id));
	}

}
