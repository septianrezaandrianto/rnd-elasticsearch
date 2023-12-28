package com.rnd.elasticsearch.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rnd.elasticsearch.dto.ProductRequest;
import com.rnd.elasticsearch.dto.Response;
import com.rnd.elasticsearch.entity.Product;
import com.rnd.elasticsearch.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private ProductRepository productRepository;

//    sample ElasticsearchOperations start
    public Response<Object> create(ProductRequest productRequest) {
        Product product = elasticsearchOperations.save(mappingProduct(productRequest));
        return Response.builder()
                .responseCode(200)
                .responseMessage("Success")
                .data(product)
                .build();
    }

    public Response<Object> createList(int from, int to) {
        List<Product> productList = new ArrayList<>();
        for(int i = from; i < to ; i++) {
            ProductRequest productRequest = new ProductRequest();
            productRequest.setName("test " + i);
            productRequest.setQuantity(i);
            productRequest.setPrice(BigDecimal.valueOf(i));

            productList.add(mappingProduct(productRequest));

        }
        elasticsearchOperations.save(productList);
        return Response.builder()
                .responseCode(200)
                .responseMessage("Success")
                .data(productList)
                .build();
    }

    public Response<Object> getDetail(String id) {
        Product product = elasticsearchOperations.get(id, Product.class);
        return Response.builder()
                .responseCode(200)
                .responseMessage("Success")
                .data(product)
                .build();
    }

    public Response<Object> update(String id, ProductRequest productRequest) {
        Product existProduct = elasticsearchOperations.get(id, Product.class);
        assert existProduct != null;
        existProduct.setName(productRequest.getName());
        existProduct.setQuantity(productRequest.getQuantity());
        existProduct.setPrice(productRequest.getPrice());
        Product updateProduct = elasticsearchOperations.save(existProduct);

        return Response.builder()
                .responseCode(200)
                .responseMessage("Success")
                .data(updateProduct)
                .build();
    }

    public Response<Object> delete(String id) {
        elasticsearchOperations.delete(id, Product.class);
        return Response.builder()
                .responseCode(200)
                .responseMessage("Success delete")
                .build();
    }

    public Response<Object> getPageFilter(String name, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        SearchHits<Product> productSearchHits = searchHit(name, pageable);

        List<Product> productList = new ArrayList<>();
        for(SearchHit<Product> product : productSearchHits.getSearchHits()) {
            Product prod = Product.builder()
                    .id(product.getId())
                    .name(product.getContent().getName())
                    .price(product.getContent().getPrice())
                    .quantity(product.getContent().getQuantity())
                    .build();
            productList.add(prod);

        }
        return Response.builder()
                .responseCode(200)
                .responseMessage("Success")
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totalData(productSearchHits.getTotalHits())
                .data(productList)
                .build();

    }

    private SearchHits<Product> searchHit(String name, Pageable pageable) {
        CriteriaQuery criteriaQuery = buildQuery(name);
        criteriaQuery.setPageable(pageable);
        return elasticsearchOperations.search(criteriaQuery, Product.class);
    }

    private CriteriaQuery buildQuery(String name) {
        Criteria criteria = new Criteria();
        if(Objects.nonNull(name)) {
            criteria.and(new Criteria("name").contains(name));
        }
        return new CriteriaQuery(criteria);
    }

//    sample ElasticsearchOperations end

    private Product mappingProduct(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();
    }
}
