package com.humanity.commerce_api.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Service
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Cliente;

    public String uploadFile(MultipartFile file) throws IOException {
        File fileObj = convertToFile(file);

        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Cliente.putObject(new PutObjectRequest(bucketName, fileName, fileObj));

        fileObj.delete();

        return "Arquivo enviado: " + fileName;
    }

    public String downloadFile(String fileName){
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hora
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = s3Cliente.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public String deleteFile(String fileName){
        s3Cliente.deleteObject(bucketName, fileName);
        return "Arquivo deletado: " + fileName;
    }

    private File convertToFile(MultipartFile file) throws IOException{
        File convertedFile = new File(file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw e;
        }
        return convertedFile;
    }
}
