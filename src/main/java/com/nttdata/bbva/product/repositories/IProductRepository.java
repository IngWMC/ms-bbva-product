package com.nttdata.bbva.product.repositories;

import com.nttdata.bbva.product.documents.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IProductRepository extends ReactiveMongoRepository<Product, String> {}
