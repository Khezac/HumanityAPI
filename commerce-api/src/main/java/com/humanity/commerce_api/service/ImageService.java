package com.humanity.commerce_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanity.commerce_api.entity.Image;
import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objMap;

    public List<Image> postImages (MultipartFile[] images, String product) throws IOException {
            Product newProduct = objMap.readValue(product, Product.class);

            return saveImage(images, newProduct);
    }

    public List<Image> saveImage (MultipartFile[] images, Product product) throws IOException {
        try {
            List<Image> savedImages = new ArrayList<>();

            for (MultipartFile image : images) {
                Image newImage = new Image();
                newImage.setBytes(image.getBytes());
                newImage.setProduct(product);
                newImage.setFileName(image.getOriginalFilename());
                newImage.setType(image.getContentType());

                imageRepo.save(newImage);
                savedImages.add(newImage);
            }

            return savedImages;
        } catch (IOException e) {
            throw new IOException("Não foi possível persistir as imagens!\nErro: " + e);
        }
    }

    public List<Image> findAllImages() {
        return imageRepo.findAll();
    }

    public Image findImageById(Long id) {
        return imageRepo.findById(id).orElse(null);
    }

    @Transactional
    public String deleteImageList(List<Long> ids) {
        try {
            ids.forEach((id) -> {
                Image imageToDelete = imageRepo.findById(id).orElseThrow(() ->
                        new NoSuchElementException("Não há imagens registradas com o id: " + id));
                imageRepo.delete(imageToDelete);
            });

            return "Lista deletada com sucesso";
        }catch (NoSuchElementException e) {
            throw new NoSuchElementException("Não foi possível deletar a lista com os IDs.\nErro: " + e);
        }
    }

}