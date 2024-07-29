package com.humanity.commerce_api.controller;

import com.humanity.commerce_api.service.StorageService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        try {
            return new ResponseEntity<>(storageService.uploadFile(file), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<String> downloadFile(@PathVariable String fileName) {
        String data = storageService.downloadFile(fileName);

        return ResponseEntity
                .ok()
                .contentLength(data.length())
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(data);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(storageService.deleteFile(fileName), HttpStatus.OK);
    }
}
