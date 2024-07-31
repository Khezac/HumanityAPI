package com.humanity.commerce_api.controller;

import com.humanity.commerce_api.DTOs.ProductDTO;
import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService service;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestParam("productInfo") String product, @RequestParam("productImages")MultipartFile[] images) throws Exception {
        return new ResponseEntity<>(service.postProduct(product, images), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts () {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById (@PathVariable Long id) {
        return new ResponseEntity<>(service.getProductById(id), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return new ResponseEntity<>(service.putProduct(product), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        return new ResponseEntity<>(service.deleteProduct(id), HttpStatus.OK);
    }
}
