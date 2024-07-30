package com.humanity.commerce_api.service;

import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    public Product postProduct (Product product) throws Exception {
        Product saveProduct = new Product();
        saveProduct = product;
        return productRepo.save(saveProduct);
    }

    public List<Product> getAllProducts () {
        List<Product> prodList = new ArrayList<>();
        prodList = productRepo.findAll();
        if(prodList.isEmpty()) {
            throw new NoSuchElementException("Não há produtos registrados");
        }
        return prodList;
    }

    public Product getProductById (Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id.toString()));
    }

    public Product putProduct (Product product) {
        Long id = product.getProduct_id();
        Product productToUpdate = new Product();

        productToUpdate = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id.toString()));

        Product updateProduct = new Product();
        updateProduct = product;
        return productRepo.save(updateProduct);
    }

    public Product deleteProduct (Long id) {
        Product productToDelete = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id.toString()));

        productRepo.delete(productToDelete);
        return productToDelete;
    }
}
