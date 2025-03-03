package com.humanity.commerce_api.controller;

import com.humanity.commerce_api.entity.Image;
import com.humanity.commerce_api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageService imgService;

    @PostMapping
    public ResponseEntity<List<Image>> addImage(@RequestParam("productImages") MultipartFile[] images, @RequestParam("productInfo") String product) throws Exception {
        return new ResponseEntity<>(imgService.postImages(images, product), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteImageList(@RequestBody List<Long> ids){
        return new ResponseEntity<>(imgService.deleteImageList(ids), HttpStatus.OK);
    }

}
