package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.exception.ImageRepositoryException;
import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Repository
@RequiredArgsConstructor
public class MinioImageDao {
    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;
    @Value("${minio.url}")
    private String minioUrl;

    @PostConstruct
    private void postConstruct() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new ImageRepositoryException("MinIO bucket could not be created", e);
        }
    }

    /**
     * @param imageName Name of the file, with extension
     * @param inputStream InputStream from the file
     * @param size Size of the file
     * @return Saved image's URL
     */
    public String create(String imageName, InputStream inputStream, long size) {
        String fileExtension = imageName.strip().substring(imageName.lastIndexOf('.') + 1).toLowerCase();
        try {
            String newImageName = new Date().getTime() + "." + fileExtension;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(newImageName)
                    .stream(inputStream, size, -1)
                    .contentType(getContentTypeFromFileName(fileExtension))
                    .build());
            return minioUrl + "/" + bucketName + "/" + newImageName;
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | MinioException e) {
            throw new ImageRepositoryException("Image could not be created", e);
        }
    }

    private String getContentTypeFromFileName(String fileExtension) {
        return switch (fileExtension) {
            case "jpe", "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "webp" -> "image/webp";
            case "tif", "tiff" -> "image/tiff";
            default -> "application/octet-stream";  // Fallback for unknown types
        };
    }

    public void delete(String imageName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(imageName)
                    .build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new ImageRepositoryException("Image could not be deleted", e);
        }
    }
}