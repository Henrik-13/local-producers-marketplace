package edu.bbte.localproducersmarketplace.service.impl;

import edu.bbte.localproducersmarketplace.exception.exceptions.ImageCouldNotBeCreatedException;
import edu.bbte.localproducersmarketplace.model.Image;
import edu.bbte.localproducersmarketplace.repository.ImageRepository;
import edu.bbte.localproducersmarketplace.repository.MinioImageDao;
import edu.bbte.localproducersmarketplace.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final MinioImageDao imageDao;

    public ImageServiceImpl(ImageRepository imageRepository, MinioImageDao imageDao) {
        this.imageRepository = imageRepository;
        this.imageDao = imageDao;
    }

    @Override
    public Optional<Image> findById(long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image create(Image image, MultipartFile file, String fileName) {
        String imageUrl;
        try {
            imageUrl = imageDao.create(fileName, file.getInputStream(), file.getSize());
        } catch (IOException e) {
            throw new ImageCouldNotBeCreatedException();
        }
        image.setPath(imageUrl);
        return imageRepository.saveAndFlush(image);
    }

    @Override
    public Image update(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Image> img = imageRepository.findById(id);
        img.ifPresent(image -> imageDao.delete(image.getPath().substring(image.getPath().lastIndexOf('/') + 1)));
        imageRepository.deleteById(id);
    }
}
