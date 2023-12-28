package com.rnd.elasticsearch.repository;

import com.rnd.elasticsearch.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    @Query("{\"source\": {\"name\": \"?0\"}}")
    Page<Product> getProductPage(String name, Pageable pageable);

}
