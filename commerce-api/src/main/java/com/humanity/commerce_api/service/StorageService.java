package com.humanity.commerce_api.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Cliente;

    public String uploadFile(MultipartFile[] files, Long id) throws IOException {
        List<MultipartFile> allFiles = Arrays.stream(files).toList();

        if(id == null) {
            throw new IOException("Não é possível enviar arquivos com ID nulo ou zerado!");
        } else {
            for (MultipartFile rawFile : allFiles) {
                File file = convertToFile(rawFile);
                try {
                    String fileName = String.format("%s/%s", id, rawFile.getOriginalFilename());

                    s3Cliente.putObject(new PutObjectRequest(bucketName, fileName, file));
                    file.delete();
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao fazer upload do arquivo: " + file + "\nErro: " + e);
                }
            }
        }

        return "Arquivos enviados com sucesso!";
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

        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw e;
        }

        return convertedFile;
    }
}
