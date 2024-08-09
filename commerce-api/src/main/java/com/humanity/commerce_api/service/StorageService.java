package com.humanity.commerce_api.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.humanity.commerce_api.DTOs.ImagesByIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    public List<ImagesByIdDTO> downloadFiles(Long id){
        List<ImagesByIdDTO> listImages = new ArrayList<>();

        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(id.toString());
        ListObjectsV2Result result = s3Cliente.listObjectsV2(req);

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hora
        expiration.setTime(expTimeMillis);
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            ImagesByIdDTO image = new ImagesByIdDTO();
            String fileName = objectSummary.getKey();
            image.setFileName(fileName);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, fileName)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);

            URL url = s3Cliente.generatePresignedUrl(generatePresignedUrlRequest);
            image.setURL(url.toString());
            listImages.add(image);
        }
        return listImages;
    }

    public String downloadByFileName(Long id, String fileName){

        String key = id + "/" + fileName;

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hora
        expiration.setTime(expTimeMillis);

        if(!s3Cliente.doesObjectExist(bucketName, key)) {
            throw new NoSuchElementException("O objeto ou o caminho inserido não existem!");
        } else {
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, key)
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);

            URL url = s3Cliente.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        }
    }

    public String downloadFirstFile(Long id){
        String path = id.toString();

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hora
        expiration.setTime(expTimeMillis);

        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(path).withMaxKeys(1);
        ListObjectsV2Result result = s3Cliente.listObjectsV2(req);

        if(result.getObjectSummaries().isEmpty()) {;
            return null;
        } else {
            S3ObjectSummary firstFile = result.getObjectSummaries().get(0);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, firstFile.getKey())
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);

            URL url = s3Cliente.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        }
    }

    public String deleteFile(Long path, String fileName){
        String url = downloadByFileName(path, fileName);

        if(url.isBlank()) {
            throw new NoSuchElementException("Não foi possível deletar objeto: " + path + "/" + fileName);
        } else {
            String deleteFile = path + "/" + fileName;
            s3Cliente.deleteObject(bucketName, deleteFile);
            return "Arquivo deletado: " + deleteFile;
        }
    }

    public String deletePath(Long path){
        List<ImagesByIdDTO> files = downloadFiles(path); // Procura o diretório passado pelo argumento da função

        if(files.isEmpty()) {
            throw new NoSuchElementException("Não foi possível encontrar diretorio: " + path);
        } else {
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request() // Requisição para pegar todos os objetos no diretório
                    .withBucketName(bucketName)
                    .withPrefix(path.toString());

            ListObjectsV2Result listObjects = s3Cliente.listObjectsV2(listObjectsV2Request);

            for (S3ObjectSummary object : listObjects.getObjectSummaries()){
                s3Cliente.deleteObject(bucketName, object.getKey());
            };

            return "Diretorio deletado: " + path;
        }
    }

    public String deleteImageList(ImagesByIdDTO[] list) throws IOException {
        try {
            for (ImagesByIdDTO image : list) {
                s3Cliente.deleteObject(bucketName, image.getFileName());
            }
            return "Lista de imagens deletada com sucesso: " + Arrays.toString(list);
        } catch(Exception e) {
            throw new IOException("Não foi possível deletar a lista de imagens!" + e);
        }
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
