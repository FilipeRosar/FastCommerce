package com.desafio.fastcommerce.application.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageUploadService {

    private final BlobServiceClient blobServiceClient;
    private final String connectionString;

    public ImageUploadService(BlobServiceClient blobServiceClient,
                              @Value("${spring.application.name}") String connectionString) {
        this.blobServiceClient = blobServiceClient;
        this.connectionString = connectionString;
    }
    public String uploadProductImage(MultipartFile file){
        try{
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(connectionString);
            if(!containerClient.exists()){
                containerClient.create();
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            blobClient.upload(file.getInputStream(), file.getSize(),true);
            return blobClient.getBlobUrl();
        } catch (IOException e){
            throw new CustomException("Falha ao processar o upload da imagem");
        }
    }
}
