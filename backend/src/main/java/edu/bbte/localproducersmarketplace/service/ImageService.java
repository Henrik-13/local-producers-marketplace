package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {
    Optional<Image> findById(long id);

    Image save(Image image, MultipartFile file, String fileName);

    Image update(Image image);

    void deleteById(Long id);

    Resource download(String path);
}

