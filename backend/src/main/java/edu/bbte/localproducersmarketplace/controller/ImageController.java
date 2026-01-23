package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.model.Image;
import edu.bbte.localproducersmarketplace.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/images")
public class ImageController {
    final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path = "/path/{path}")
    public ResponseEntity<Resource> getImageByPath(@PathVariable String path) {
        try {
            MediaType mediaType = switch (path.substring(path.lastIndexOf('.') + 1)) {
                case "jpe", "jpg", "jpeg", "jif", "jfif" -> MediaType.IMAGE_JPEG;
                case "png" -> MediaType.IMAGE_PNG;
                default -> MediaType.APPLICATION_OCTET_STREAM;
            };
            Resource imageResource = imageService.download(path);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path + "\"")
                    .contentType(mediaType)
                    .body(imageResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource> getImageByID(@PathVariable Long id) {
        Optional<Image> image = imageService.findById(id);
        if (image.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            try {
                String path = image.get().getName();
                MediaType mediaType = switch (path.substring(path.lastIndexOf('.') + 1)) {
                    case "jpe", "jpg", "jpeg", "jif", "jfif" -> MediaType.IMAGE_JPEG;
                    case "png" -> MediaType.IMAGE_PNG;
                    default -> MediaType.APPLICATION_OCTET_STREAM;
                };
                Resource imageResource = imageService.download(path);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path + "\"")
                        .contentType(mediaType)
                        .body(imageResource);
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteImage(@PathVariable Long id) {
        Optional<Image> image = imageService.findById(id);
        if (image.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            imageService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}