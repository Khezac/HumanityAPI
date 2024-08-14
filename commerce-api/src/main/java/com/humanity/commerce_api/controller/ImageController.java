package com.humanity.commerce_api.controller;

import com.humanity.commerce_api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    ImageService imgService;

    @DeleteMapping
    public ResponseEntity<String> deleteImageList(@RequestBody List<Long> ids){
        return new ResponseEntity<>(imgService.deleteImageList(ids), HttpStatus.OK);
    }

}
