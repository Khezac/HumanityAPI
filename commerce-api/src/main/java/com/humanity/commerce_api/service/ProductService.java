package com.humanity.commerce_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanity.commerce_api.DTOs.ImageDTO;
import com.humanity.commerce_api.DTOs.ImagesByIdDTO;
import com.humanity.commerce_api.DTOs.ProductDTO;
import com.humanity.commerce_api.DTOs.ProductWithEveryImageDTO;
import com.humanity.commerce_api.entity.Image;
import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    StorageService storageService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objMap;

    public Product postProduct (String product, MultipartFile[] images) {
        try {
            Product newProduct = objMap.readValue(product, Product.class);
            Product savedProduct = productRepo.save(newProduct);
            imageService.saveImage(images, savedProduct);


            return savedProduct;
        } catch(Exception e) {
            throw new RuntimeException("Não foi possível cadastrar produto: " + product);
        }
    }

    public List<ProductDTO> getAllProducts () {
        try {
            List<Product> prodList = productRepo.findAll();

            List<ProductDTO> prodDtoList = new ArrayList<>();

            for (Product product : prodList) {
                List<ImageDTO> images = new ArrayList<>();

                ProductDTO productDTO = new ProductDTO();

                // TRANSFORMAR ESSE BLOCO EM UM MÈTODO E APENAS CHAMAR O METODO
                productDTO.setProduct_id(product.getProduct_id());
                productDTO.setName(product.getName());
                productDTO.setDescription(product.getDescription());
                productDTO.setSize(product.getSize());
                productDTO.setGender(product.getGender());
                productDTO.setUnit_price(product.getUnit_price());
                productDTO.setCategory(product.getCategory());

                Image image = imageService.findImageById(product.getProduct_id());
                ImageDTO imageDto = modelMapper.map(image, ImageDTO.class);
                images.add(imageDto);

                productDTO.setImages(images);
                prodDtoList.add(productDTO);
            }

            return prodDtoList;
        } catch (Exception e){
            throw new RuntimeException("Ocorreu um erro ao tentar acessar a lista dos produtos: " + e);
        }
    }

    public ProductWithEveryImageDTO getProductById (Long id) {
        Product productEntity = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id));

        ProductWithEveryImageDTO productDTO = modelMapper.map(productEntity, ProductWithEveryImageDTO.class);

        List<ImagesByIdDTO> imagesUrls;
        try {
            imagesUrls = storageService.downloadFiles(productEntity.getProduct_id());
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível captar imagens desse produto!");
        }
        productDTO.setImageURL(imagesUrls);

        return productDTO;
    }

    public Product putProduct (Product product) {
        try {
            Long id = product.getProduct_id();

            productRepo.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id));

            return productRepo.save(product);
        } catch(Exception e) {
            throw new RuntimeException("Não foi possível editar produto: " + product);
        }
    }

    public ProductWithEveryImageDTO deleteProduct (Long id) {
        Product productToDelete = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id));

        ProductWithEveryImageDTO productDTO = modelMapper.map(productToDelete, ProductWithEveryImageDTO.class);

        List<ImagesByIdDTO> imagesUrls;

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

    public Product deleteOnlyInfo(Long id) throws Exception {
        Product productToDelete = productRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id));
        try {
            productRepo.delete(productToDelete);
            return productToDelete;
        } catch(Exception e) {
            throw new Exception("Não foi possível deletar informações do produto com id: " + id);
        }
    }

}
