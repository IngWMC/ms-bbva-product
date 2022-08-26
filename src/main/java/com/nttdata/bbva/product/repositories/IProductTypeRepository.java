package com.nttdata.bbva.product.repositories;

import com.nttdata.bbva.product.documents.ProductType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IProductTypeRepository extends ReactiveMongoRepository<ProductType, String> {}
