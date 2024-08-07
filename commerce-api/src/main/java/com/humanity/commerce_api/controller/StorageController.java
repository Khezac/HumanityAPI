package com.humanity.commerce_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humanity.commerce_api.DTOs.ImagesByIdDTO;
import com.humanity.commerce_api.entity.Product;
import com.humanity.commerce_api.service.StorageService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    ObjectMapper objMap;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "files") MultipartFile[] files,@RequestParam("id") String productId) {
        try {
            Product product = objMap.readValue(productId, Product.class);
            return new ResponseEntity<>(storageService.uploadFile(files, product.getProduct_id()), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/download/{path}")
    public ResponseEntity<List<ImagesByIdDTO>> downloadFile(@PathVariable Long path) {
        List<ImagesByIdDTO> data = storageService.downloadFiles(path);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
    @GetMapping("/download/firstFile/{path}")
    public ResponseEntity<String> downloadFirstFile(@PathVariable Long path) {
        String data = storageService.downloadFirstFile(path);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

    @DeleteMapping("/delete/{path}/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable Long path,@PathVariable String fileName) {
        return new ResponseEntity<>(storageService.deleteFile(path,fileName), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{path}")
    public ResponseEntity<String> deletePath(@PathVariable Long path) {
        return new ResponseEntity<>(storageService.deletePath(path), HttpStatus.OK);
    }
}
