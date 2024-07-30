package com.humanity.commerce_api.controller;

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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "files") MultipartFile[] files,@RequestParam("id") Long id) {
        try {
            return new ResponseEntity<>(storageService.uploadFile(files, id), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/download/{path}")
    public ResponseEntity<List<String>> downloadFile(@PathVariable Long path) {
        List<String> data = storageService.downloadFile(path);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
    @GetMapping("/download/{path}/{fileName}")
    public ResponseEntity<String> downloadFile(@PathVariable Long path, @PathVariable String fileName) {
        String data = storageService.downloadByFileName(path, fileName);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }


    @DeleteMapping("/delete/{path}/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable Long path,@PathVariable String fileName) {
        return new ResponseEntity<>(storageService.deleteFile(path,fileName), HttpStatus.OK);
    }
}
