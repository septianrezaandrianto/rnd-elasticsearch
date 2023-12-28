package com.rnd.elasticsearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rnd.elasticsearch.dto.ProductRequest;
import com.rnd.elasticsearch.dto.Response;
import com.rnd.elasticsearch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/create")
    public ResponseEntity<Response<Object>> create(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.create(productRequest));
    }

    @GetMapping(value = "/createList")
    public ResponseEntity<Response<Object>> createList(@RequestParam(value = "from") int from,
                                                       @RequestParam(value = "to") int to) {
        return ResponseEntity.ok(productService.createList(from,to));
    }

    @GetMapping(value = "/getDetail")
    public ResponseEntity<Response<Object>> getDetail(@RequestParam(value = "id") String id) {
        return ResponseEntity.ok(productService.getDetail(id));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Response<Object>> getDetail(@RequestParam(value = "id") String id, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.update(id, productRequest));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Response<Object>> delete(@RequestParam(value = "id") String id) {
        return ResponseEntity.ok(productService.delete(id));
    }

    @GetMapping(value = "/getPageFilter")
    public ResponseEntity<Response<Object>> getPageFilter(@RequestParam(value = "name") String name,
                                                          @RequestParam(value = "pageNumber") int pageNumber,
                                                          @RequestParam(value = "pageSize") int pageSize) throws JsonProcessingException {
        return ResponseEntity.ok(productService.getPageFilter(name,pageNumber, pageSize));
    }


}
