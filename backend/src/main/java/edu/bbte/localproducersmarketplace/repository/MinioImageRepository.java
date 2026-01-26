package edu.bbte.localproducersmarketplace.repository;

import edu.bbte.localproducersmarketplace.exception.ImageRepositoryException;
import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Repository
@RequiredArgsConstructor
public class MinioImageRepository {
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
     * @param filename Name of the saved image, with extension
     * @return Saved image Resource
     */
    public Resource getImage(final String filename) {
        try {
            InputStream inputStream =
                    minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(filename)
                                    .build());
            return new InputStreamResource(inputStream);
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | MinioException e) {
            throw new ImageRepositoryException("Image could not be found", e);
        }
    }

    /**
     * @param img Image resource as MultipartFile
     * @param imageName Image name
     * @return Saved image's path
     */
    public String save(MultipartFile img, String imageName) {
        String fileExtension = imageName.strip().substring(imageName.lastIndexOf('.') + 1).toLowerCase();
        try {
            String newImageName = new Date().getTime() + "." + fileExtension;
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(newImageName)
                            .stream(img.getInputStream(), img.getSize(), -1)
                            .contentType(img.getContentType())
                            .build());
            return newImageName;
        } catch (InvalidKeyException | IOException | NoSuchAlgorithmException | MinioException e) {
            throw new ImageRepositoryException("Image could not be saved", e);
        }
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
