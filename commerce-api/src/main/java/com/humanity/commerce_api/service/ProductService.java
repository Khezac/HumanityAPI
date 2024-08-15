package com.humanity.commerce_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanity.commerce_api.DTOs.ImageDTO;
import com.humanity.commerce_api.DTOs.ImagesByIdDTO;
import com.humanity.commerce_api.DTOs.ProductDTO;
import com.humanity.commerce_api.DTOs.ProductWithEveryImageDTO;
import com.humanity.commerce_api.entity.Image;
import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public Product postProduct (String product, MultipartFile[] images) throws IOException {
        try {
            Product newProduct = objMap.readValue(product, Product.class);
            Product savedProduct = productRepo.save(newProduct);
            imageService.saveImage(images, savedProduct);

            return savedProduct;
        } catch(Exception e) {
            throw new IOException("Não foi possível cadastrar produto: " + product + "\nErro: " + e);
        }
    }
    @Transactional
    public List<ProductDTO> getAllProducts () {
        try {
            List<Product> prodList = productRepo.findAll();

            List<ProductDTO> prodDtoList = new ArrayList<>();

            for (Product product : prodList) {
                ProductDTO productDTO = ProductToDto(product);

                List<ImageDTO> imagesDto = new ArrayList<>();

                if(!product.getImages().isEmpty()){
                    imagesDto.add(ImageToDto(product.getImages().getFirst()));

                    productDTO.setImages(imagesDto);
                }
                prodDtoList.add(productDTO);
            }

            return prodDtoList;
        } catch (Exception e){
            throw new RuntimeException("Ocorreu um erro ao tentar acessar a lista dos produtos: \n" + e);
        }
    }

    @Transactional
    public ProductDTO getProductById (Long id) {
        try {
            Product productEntity = productRepo.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id));

            ProductDTO productDTO = ProductToDto(productEntity);

            List<ImageDTO> images = productEntity.getImages().stream().map(this::ImageToDto).collect(Collectors.toList());

            productDTO.setImages(images);
            return productDTO;
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível captar imagens desse produto!\nErro: " + e);
        }
    }

    public Product putProduct (Product product) {
        try {
            Long id = product.getProduct_id();

            productRepo.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id));

            return productRepo.save(product);
        } catch(Exception e) {
            throw new RuntimeException("Não foi possível editar produto de id: " + product.getProduct_id() + "\nErro: " + e);
        }
    }

    @Transactional
    public Product deleteProduct (Long id) {
        try {
            Product productToDelete = productRepo.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Não há produtos registrados com o id: " + id));

            productRepo.delete(productToDelete);

            return productToDelete;
        } catch (Exception e){
            throw new NoSuchElementException("Não foi possível deletar produto com id: " + id + "\nErro: " + e);
        }
    }

    public ProductDTO ProductToDto (Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setProduct_id(product.getProduct_id());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setSize(product.getSize());
        productDTO.setGender(product.getGender());
        productDTO.setUnit_price(product.getUnit_price());
        productDTO.setCategory(product.getCategory());

        return productDTO;
    }

    @Transactional
    public ImageDTO ImageToDto (Image image) {
        ImageDTO imageDto = new ImageDTO();

        imageDto.setImage_id(image.getImage_id());
        imageDto.setBytes(image.getBytes());
        imageDto.setType(image.getType());
        imageDto.setFileName(image.getFileName());

//        return modelMapper.map(image, ImageDTO.class);
        return imageDto;
    }

}
