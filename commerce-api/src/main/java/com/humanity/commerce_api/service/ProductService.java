package com.humanity.commerce_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanity.commerce_api.DTOs.ProductDTO;
import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    StorageService storageService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objMap;

    public Product postProduct (String product, MultipartFile[] images) throws Exception {
        Product newProduct = new Product();
        try {
            newProduct = objMap.readValue(product, Product.class);
            Product savedProduct = productRepo.save(newProduct);
            storageService.uploadFile(images, savedProduct.getProduct_id());
            return savedProduct;
        } catch(Exception e) {
            throw new RuntimeException("Não foi possível cadastrar produto: " + product);
        }
    }

    public List<ProductDTO> getAllProducts () {
        List<Product> prodList = productRepo.findAll();;

        List<ProductDTO> prodDtoList = new ArrayList<>();
        List<String> imagesUrls = new ArrayList<>();

        if(prodList.isEmpty()) {
            throw new NoSuchElementException("Não há produtos registrados");
        } else {
            for (Product product : prodList) {
                ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                try {
                    String imageUrl = storageService.downloadFirstFile(product.getProduct_id());
                    imagesUrls.add(imageUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Não foi possível captar imagens desse produto!");
                }
                productDTO.setImageURL(imagesUrls);
                prodDtoList.add(productDTO);
            }
        }
        return prodDtoList;
    }

    public ProductDTO getProductById (Long id) {
        Product productEntity = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id.toString()));

        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);

        List<String> imagesUrls = new ArrayList<>();
        try {
            imagesUrls = storageService.downloadFiles(productEntity.getProduct_id());
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível captar imagens desse produto!");
        }
        productDTO.setImageURL(imagesUrls);

        return productDTO;
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

    public ProductDTO deleteProduct (Long id) {
        Product productToDelete = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id.toString()));

        ProductDTO productDTO = modelMapper.map(productToDelete, ProductDTO.class);

        List<String> imagesUrls = new ArrayList<>();

        try {
            imagesUrls = storageService.downloadFiles(productToDelete.getProduct_id());
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível captar imagens desse produto!");
        }

        productDTO.setImageURL(imagesUrls);

        productRepo.delete(productToDelete);
        storageService.deletePath(id);

        return productDTO;
    }


}
