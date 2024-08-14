package com.humanity.commerce_api.service;

import com.humanity.commerce_api.entity.Image;
import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepo;

    public void saveImage (MultipartFile[] images, Product product) {
        try {
            for (MultipartFile image : images) {
                Image newImage = new Image();
                newImage.setImage_id(product.getProduct_id());
                newImage.setBytes(image.getBytes());
                newImage.setProduct(product);
                newImage.setFileName(image.getOriginalFilename());
                newImage.setType(image.getContentType());

                imageRepo.save(newImage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Image> findAllImages() {
        return imageRepo.findAll();
    }

    public Image findImageById(Long id) {
        return imageRepo.findById(id).orElse(null);
    }

}